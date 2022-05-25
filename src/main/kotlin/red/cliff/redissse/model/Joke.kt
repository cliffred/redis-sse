package red.cliff.redissse.model

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Joke(val setup: String, val punchline: String)

data class JokeWithId(val id: Int, val joke: Joke)
