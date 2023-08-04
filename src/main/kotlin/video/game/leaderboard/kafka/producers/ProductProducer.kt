package video.game.leaderboard.kafka.producers

import org.apache.kafka.clients.producer.ProducerRecord
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import video.game.Product


@Component
class ProductProducer (
    private val kafkaTemplate: KafkaTemplate<String, Product>
) : ApplicationListener<ApplicationStartedEvent> {
    private val logger = LoggerFactory.getLogger(ProductProducer::class.java)

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        val products = listOf(Product(1,"avenger 1"), Product(2,"avenger 2"),Product(3,"avenger 3"))
        products.forEach {
            val record = ProducerRecord("products-topic", it.id.toString(), it)
            logger.info("Generated record {} for product", record)
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