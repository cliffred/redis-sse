package red.cliff.redissse.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer
import red.cliff.redissse.model.Joke


@Configuration
class RedisConfig {

    @Bean
    fun jokeTemplate(
        lettuceConnectionFactory: LettuceConnectionFactory,
        objectMapper: ObjectMapper
    ): ReactiveRedisTemplate<String, Joke> {
        val valueSerializer = Jackson2JsonRedisSerializer(Joke::class.java).apply {
            setObjectMapper(objectMapper)
        }

        val serializationContext =
            RedisSerializationContext.newSerializationContext<String, Joke>(RedisSerializer.string())
                .value(valueSerializer)
                .build()
        return ReactiveRedisTemplate(lettuceConnectionFactory, serializationContext)
    }
}
