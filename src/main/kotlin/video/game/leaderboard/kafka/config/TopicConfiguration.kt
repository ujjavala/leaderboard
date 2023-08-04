package video.game.leaderboard.kafka.config

import org.apache.kafka.clients.admin.NewTopic
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.TopicBuilder

@Configuration
class TopicConfiguration(
    @Value("\${topics.score-events.name}") private val inputTopicMockName: String,
    @Value("\${topics.players.name}") private val outputTopicRaw: String,
    @Value("\${topics.products.name}") private val outputTopicAggregated: String,
    @Value("\${topics.partitions}") private val topicPartitions: Int,
    @Value("\${topics.replicas}") private val topicReplicas: Int
) {

    @Bean
    fun inputTopicMock(): NewTopic {
        return TopicBuilder.name(inputTopicMockName)
            .partitions(topicPartitions)
            .replicas(topicReplicas)
            .build()
    }

    @Bean
    fun outputTopicRaw(): NewTopic {
        return TopicBuilder.name(outputTopicRaw)
            .partitions(topicPartitions)
            .replicas(topicReplicas)
            .build()
    }

    @Bean
    fun outputTopicAggregated(): NewTopic {
        return TopicBuilder.name(outputTopicAggregated)
            .partitions(topicPartitions)
            .replicas(topicReplicas)
            .build()
    }
}
