package red.cliff.redissse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.jackson.jackson
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JokeClient(
    @Value("\${joke.url}")
    private val url: String
) {

    private val client = HttpClient() {
        expectSuccess = true
        install(ContentNegotiation) {
            jackson()
        }
    }


    suspend fun fetchJoke(): Joke = client.get(url).body()
}

@JsonIgnoreProperties(ignoreUnknown = true)
data class Joke(val setup: String, val punchline: String)
