package video.game.leaderboard.kafka.producers

import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import video.game.Player


@Component
class PlayerProducer (
 private val kafkaTemplate: KafkaTemplate<String, Player>
) : ApplicationListener<ApplicationStartedEvent> {
    private val logger = LoggerFactory.getLogger(PlayerProducer::class.java)

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        val players = listOf(Player(1,"iron man"), Player(2,"captain america"),Player(3,"hulk"))
        players.forEach {
            val record = ProducerRecord("players-topic", it.id.toString(), it)
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