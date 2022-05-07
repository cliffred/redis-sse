package red.cliff.redissse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@EnableScheduling
@SpringBootApplication
class RedisSseApplication

fun main(args: Array<String>) {
	runApplication<RedisSseApplication>(*args)
}
