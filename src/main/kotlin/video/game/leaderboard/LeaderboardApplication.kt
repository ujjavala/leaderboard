package video.game.leaderboard

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafkaStreams

@SpringBootApplication
@EnableKafkaStreams
class LeaderboardApplication
fun main(args: Array<String>) {
    @Suppress("SpreadOperator")
    runApplication<LeaderboardApplication>(*args)
}
