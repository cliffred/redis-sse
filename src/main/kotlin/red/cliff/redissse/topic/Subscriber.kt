package red.cliff.redissse.topic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import org.springframework.stereotype.Service
import red.cliff.redissse.repo.JokeRepository
import javax.annotation.PostConstruct

@Service
class Subscriber(
    private val jokeRepository: JokeRepository
) {

    @PostConstruct
    fun subscribe() {
        jokeRepository.listen()
            .onEach { println(it) }
            .catch { System.err.println(it.message) }
            .onCompletion { println("DONE") }
            .launchIn(CoroutineScope(Dispatchers.Default))
    }

}
