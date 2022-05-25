package red.cliff.redissse.topic

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import org.springframework.stereotype.Component
import red.cliff.redissse.model.JokeWithId
import red.cliff.redissse.repo.JokeRepository
import java.util.concurrent.atomic.AtomicInteger
import javax.annotation.PostConstruct

@Component
class ChannelManager(
    private val jokeRepository: JokeRepository
) {
    private val id = AtomicInteger(0)

    val channels = mutableMapOf<Int, Channel<JokeWithId>>()

    fun newChannel(): Flow<JokeWithId> {
        val id = id.incrementAndGet()
        val channel = Channel<JokeWithId>().apply { invokeOnClose { channels.remove(id) } }
        channels[id] = channel
        return channel.consumeAsFlow()
    }

    @PostConstruct
    fun subscribe() {
        jokeRepository.listen()
            .onEach { joke ->
                println("Sending to ${channels.size} channels")
                channels.forEach { (id, channel) ->
                    channel.send(JokeWithId(id, joke))
                }
            }
            .catch { it.printStackTrace() }
            .onCompletion { println("DONE") }
            .launchIn(CoroutineScope(Dispatchers.Default + SupervisorJob()))
    }
}
