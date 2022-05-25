package red.cliff.redissse.web

import kotlinx.coroutines.flow.Flow
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import red.cliff.redissse.model.JokeWithId
import red.cliff.redissse.topic.ChannelManager

@RestController
class JokeController(
    private val channelManager: ChannelManager
) {
    @GetMapping(path = ["/jokes"], produces = [MediaType.TEXT_EVENT_STREAM_VALUE])
    fun jokes(): Flow<JokeWithId> = channelManager.newChannel()
}
