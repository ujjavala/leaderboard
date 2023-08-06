package video.game.leaderboard.kafka.producers

import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import video.game.ScoreEvent

@Component
class ScoreEventRepository(
    private val kafkaTemplate: KafkaTemplate<String, ScoreEvent>,
    @Value("\${topics.score-events.name}") private val scoreEventsTopic: String
) : ApplicationListener<ApplicationStartedEvent> {
    private val logger = LoggerFactory.getLogger(ScoreEventRepository::class.java)
    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        populateScoreEvents()
    }

    private fun populateScoreEvents() {
        val scoreEvents = listOf(ScoreEvent(1, 2, 432), ScoreEvent(2, 1, 496))
        scoreEvents.forEach {
            val record = ProducerRecord(scoreEventsTopic, it.playerId.toString(), it)
            logger.info("Generated record for score event{}", record)
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

