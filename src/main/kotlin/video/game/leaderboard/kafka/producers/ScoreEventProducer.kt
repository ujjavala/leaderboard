package video.game.leaderboard.kafka.producers

import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import video.game.ScoreEvent

@Component
class ScoreEventProducer (
    private val kafkaTemplate: KafkaTemplate<String,ScoreEvent>
) : ApplicationListener<ApplicationStartedEvent> {
    private val logger = LoggerFactory.getLogger(ScoreEventProducer::class.java)
    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        val scoreEvents = listOf(ScoreEvent(1,2,432), ScoreEvent(2,1,496))
            scoreEvents.forEach {
            val record = ProducerRecord("score-events-topic","", it)
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

