package red.cliff.redissse

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.listenToChannelAsFlow
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct

@Service
class Consumer(
    private val redisTemplate: ReactiveRedisOperations<String, Joke>,
    @Value("\${topic.name.joke-channel}")
    private val jokeTopic: String
) {

    @PostConstruct
    fun consume() {
        redisTemplate.listenToChannelAsFlow(jokeTopic)
            .map { it.message }
            .onEach { println(it) }
            .catch { System.err.println(it.message) }
            .onCompletion { println("DONE") }
            .launchIn(CoroutineScope(Dispatchers.Default))
    }

}
