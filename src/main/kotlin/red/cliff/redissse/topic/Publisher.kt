package red.cliff.redissse.topic

import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import red.cliff.redissse.client.JokeClient
import red.cliff.redissse.repo.JokeRepository

@Service
class Publisher(
    private val jokeClient: JokeClient,
    private val jokeRepository: JokeRepository
) {

    @Scheduled(fixedRate = 3000)
    fun publishJoke(): Unit = runBlocking {
        val joke = jokeClient.fetchJoke()
        jokeRepository.publishJoke(joke)
    }

}
