package red.cliff.redissse

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.reactive.asFlow
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Sinks
import java.util.concurrent.atomic.AtomicInteger
import javax.annotation.PostConstruct

@RestController
class JokeController(
    private val sinkManager: SinkManager
) {
    @GetMapping(path = ["/jokes"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun jokes(): Flow<JokeWithId> = sinkManager.newSink()
}

@Component
class SinkManager(
    private val jokeRepository: JokeRepository
) {
    private val id = AtomicInteger(0)

    val sinks = mutableListOf<Pair<Int, Sinks.Many<JokeWithId>>>()

    fun newSink(): Flow<JokeWithId> {
        val sink = Sinks.many().replay().latest<JokeWithId>()
        sinks += Pair(id.incrementAndGet(), sink)
        return sink.asFlux().asFlow()
    }

    @PostConstruct
    fun subscribe() {
        jokeRepository.listen()
            .onEach { joke ->
                sinks.forEach { it.second.tryEmitNext(JokeWithId(it.first, joke)) }
            }
            .catch { it.printStackTrace() }
            .onCompletion { println("DONE") }
            .launchIn(CoroutineScope(Dispatchers.Default))
    }
}

data class JokeWithId(val id: Int, val joke: Joke)
