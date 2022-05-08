package red.cliff.redissse

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class Publisher(
    private val jokeClient: JokeClient,
    private val redisTemplate: ReactiveRedisOperations<String, Joke>,
    @Value("\${topic.joke-channel}")
    private val jokeTopic: String
) {

    @Scheduled(fixedRate = 3000)
    fun publishJoke() = runBlocking {
        val joke = jokeClient.fetchJoke()
        redisTemplate.convertAndSend(jokeTopic, joke).awaitSingle()
    }

}
