package red.cliff.redissse

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.listenToChannelAsFlow
import org.springframework.stereotype.Repository

@Repository
class JokeRepository(
    private val redisTemplate: ReactiveRedisOperations<String, Joke>,
    @Value("\${topic.joke-channel}")
    private val jokeTopic: String
) {
    suspend fun publishJoke(joke: Joke) {
        redisTemplate.convertAndSend(jokeTopic, joke).awaitSingle()
    }

    fun listen(): Flow<Joke> = redisTemplate.listenToChannelAsFlow(jokeTopic)
        .map { it.message }
}
