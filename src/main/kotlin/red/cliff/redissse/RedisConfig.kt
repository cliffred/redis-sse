package red.cliff.redissse

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.ReactiveRedisOperations
import org.springframework.data.redis.core.ReactiveRedisTemplate
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import org.springframework.data.redis.serializer.RedisSerializer


@Configuration
class RedisConfig {

    @Bean
    fun jokeTemplate(lettuceConnectionFactory: LettuceConnectionFactory): ReactiveRedisOperations<String, Joke> {
        val valueSerializer = Jackson2JsonRedisSerializer(Joke::class.java)
        valueSerializer.setObjectMapper(jacksonObjectMapper())
        val serializationContext =
            RedisSerializationContext.newSerializationContext<String, Joke>(RedisSerializer.string())
                .value(valueSerializer)
                .build()
        return ReactiveRedisTemplate(lettuceConnectionFactory, serializationContext)
    }
}
