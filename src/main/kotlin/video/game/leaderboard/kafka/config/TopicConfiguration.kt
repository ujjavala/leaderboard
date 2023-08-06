package video.game.leaderboard.kafka.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class TopicConfiguration(
    @Value("\${topics.score-events.name}") private val scoreEventsTopic: String,
    @Value("\${topics.players.name}") private val playersTopic: String,
    @Value("\${topics.products.name}") private val productsTopic: String,
    @Value("\${topics.partitions}") private val topicPartitions: Int,
    @Value("\${topics.replicas}") private val topicReplicas: Int
) {

    @Bean
    fun scoreEventsTopic(): NewTopic {
        return TopicBuilder.name(scoreEventsTopic)
            .partitions(topicPartitions)
            .replicas(topicReplicas)
            .build()
    }

    @Bean
    fun playersTopic(): NewTopic {
        return TopicBuilder.name(playersTopic)
            .partitions(topicPartitions)
            .replicas(topicReplicas)
            .build()
    }

    @Bean
    fun productsTopic(): NewTopic {
        return TopicBuilder.name(productsTopic)
            .partitions(topicPartitions)
            .replicas(topicReplicas)
            .build()
    }
}
