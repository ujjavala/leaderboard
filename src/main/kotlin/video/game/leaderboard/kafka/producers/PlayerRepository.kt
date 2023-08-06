package video.game.leaderboard.kafka.producers

import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import video.game.Player

@Component
class PlayerRepository(
    private val kafkaTemplate: KafkaTemplate<String, Player>,
    @Value("\${topics.players.name}") private val playersTopic: String
) : ApplicationListener<ApplicationStartedEvent> {
    private val logger = LoggerFactory.getLogger(PlayerRepository::class.java)

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        populatePlayers()
    }

    private fun populatePlayers() {
        val players = listOf(Player(1, "iron man"), Player(2, "captain america"), Player(3, "hulk"))
        players.forEach {
            val record = ProducerRecord(playersTopic, it.id.toString(), it)
            logger.info("Generated record for player{}", record)
            kafkaTemplate
                .send(record)
                .whenComplete { result, error ->
                    if (error != null) {
                        logger.error("An error occurred {}", error.suppressedExceptions)
                    } else {
                        logger.info("Successfully sent to kafka {}", result)
                    }
                }

        }
    }
}