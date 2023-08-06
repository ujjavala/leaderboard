package video.game.leaderboard.kafka.processors

import io.confluent.kafka.streams.serdes.avro.SpecificAvroSerde
import org.apache.kafka.common.serialization.Serdes
import org.apache.kafka.streams.StreamsBuilder
import org.apache.kafka.streams.kstream.Consumed
import org.apache.kafka.streams.kstream.Joined
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.context.event.ApplicationStartedEvent
import org.springframework.context.ApplicationListener
import org.springframework.stereotype.Component
import video.game.Player
import video.game.Product
import video.game.ScoreEvent
import java.util.*


@Component
class LeaderBoardService(
    streamsBuilder: StreamsBuilder,
    @Value("\${topics.score-events.name}") private val scoreEventsTopic: String,
    @Value("\${topics.players.name}") private val playersTopic: String,
    @Value("\${topics.products.name}") private val productsTopic: String,
    @Value("\${spring.kafka.properties.schema.registry.url}") private val schemaRegistryUrl: String
) : ApplicationListener<ApplicationStartedEvent> {
    private val logger = LoggerFactory.getLogger(LeaderBoardService::class.java)


    private final val serdeConfig = Collections.singletonMap("schema.registry.url", schemaRegistryUrl)


    private val scoreEvents = streamsBuilder
        .stream(
            scoreEventsTopic,
            Consumed.with(Serdes.String(), getScoreEventSerde(serdeConfig))
        )
    private val players = streamsBuilder
        .table(
            playersTopic,
            Consumed.with(Serdes.String(), getPlayerSerde(serdeConfig))
        )
    private val products = streamsBuilder
        .globalTable(
            productsTopic,
            Consumed.with(Serdes.String(), getProductSerde(serdeConfig))
        )

    private val joinedScoreEventPlayer: Joined<String, ScoreEvent, Player> =
        Joined.with(Serdes.String(), SpecificAvroSerde(), SpecificAvroSerde())

    private final fun getScoreEventSerde(serdeConfig: MutableMap<String, String>): SpecificAvroSerde<ScoreEvent> {
        val serde = SpecificAvroSerde<ScoreEvent>()
        serde.configure(serdeConfig, false)
        return serde
    }

    private final fun getPlayerSerde(serdeConfig: MutableMap<String, String>): SpecificAvroSerde<Player> {
        val serde = SpecificAvroSerde<Player>()
        serde.configure(serdeConfig, false)
        return serde
    }

    private final fun getProductSerde(serdeConfig: MutableMap<String, String>): SpecificAvroSerde<Product> {
        val serde = SpecificAvroSerde<Product>()
        serde.configure(serdeConfig, false)
        return serde
    }

    override fun onApplicationEvent(event: ApplicationStartedEvent) {
        logger.info("####### Score events ##### {} ", scoreEvents)
        logger.info("####### Players ##### {} ", players)
        logger.info("####### Products ##### {} ", products)
        logger.info("####### Joins ##### {} ", joinedScoreEventPlayer)
    }
}