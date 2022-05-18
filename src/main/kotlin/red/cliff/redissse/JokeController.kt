package red.cliff.redissse

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
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import java.util.concurrent.atomic.AtomicInteger
import javax.annotation.PostConstruct

@RestController
class JokeController(
    private val flowManager: FlowManager
) {
    @GetMapping(path = ["/jokes"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun jokes(): Flow<JokeWithId> = flowManager.newChannel()
}

@Component
class FlowManager(
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

data class JokeWithId(val id: Int, val joke: Joke)
