package red.cliff.redissse

import kotlinx.coroutines.runBlocking
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.ReactiveRedisOperations
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
        runBlocking {
            redisTemplate.listenToChannel(jokeTopic)
                .map { it.message }
                .subscribe { println(it) }
        }
    }

}
