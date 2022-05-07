package red.cliff.redissse

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RedisSseApplication

fun main(args: Array<String>) {
	runApplication<RedisSseApplication>(*args)
}
