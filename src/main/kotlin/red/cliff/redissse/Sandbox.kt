package red.cliff.redissse

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.produce
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun main() {
    val flow = flow {
        repeat(3) {
            println("Computing FLOW value")
            val x = computeNextValue()
            emit(x)
        }
    }

    coroutineScope {
        launch {
            val channel = produce(capacity = 2) {
                while (true) {
                    println("Computing CHANNEL value")
                    val x = computeNextValue()
                    send(x)
                }
            }
            println("CHANNEL event ${channel.receive()}")
        }

        launch {
            println("Collecting FLOW")
            flow
                .onEach { println("FLOW event $it") }
                .collect()
        }
    }
    println("Done")
}

var x = 0
fun computeNextValue(): Int {
    return x++
}
