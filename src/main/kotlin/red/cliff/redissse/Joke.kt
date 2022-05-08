package red.cliff.redissse

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class Joke(val setup: String, val punchline: String)
