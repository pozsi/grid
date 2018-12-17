package lib

import java.util.UUID

import com.gu.mediaservice.model._
import helpers.Fixtures
import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.concurrent.{Eventually, ScalaFutures}
import org.scalatest.{BeforeAndAfterAll, FreeSpec, Matchers}
import play.api.Configuration
import play.api.libs.json.{JsDefined, Json}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

class ElasticSearch6Test extends FreeSpec with Matchers with Fixtures with BeforeAndAfterAll with Eventually with ScalaFutures {

  val thrallConfig = new ThrallConfig(Configuration.from(Map(
    "es.cluster" -> "media-service-test",
    "es.port" -> "9206",
    "es.index.aliases.write" -> "writeAlias"
  )))

  val thrallMetrics = new ThrallMetrics(thrallConfig)

  val ES = new ElasticSearch6(thrallConfig, thrallMetrics)

  val oneHundredMilliseconds = Duration(100, MILLISECONDS)
  val fiveSeconds = Duration(5, SECONDS)

  "Elasticsearch" - {

    "indexing" - {
      "can index and retrieve images by id" in {
        val id = UUID.randomUUID().toString
        val image = createImageForSyndication(id = UUID.randomUUID().toString, true, Some(DateTime.now()), None)

        Await.result(Future.sequence(ES.indexImage(id, Json.toJson(image))), fiveSeconds) // TODO why is index past in? Is it different to image.id and if so why?

        eventually(timeout(fiveSeconds), interval(oneHundredMilliseconds))(reloadedImage(id).map(_.id) shouldBe Some(image.id))

        reloadedImage(id).get.id shouldBe image.id
      }
    }

    "user metadata" - {
      "can update user metadata for an existing image" in {
        val id = UUID.randomUUID().toString
        val imageWithBoringMetadata = createImageForSyndication(id = UUID.randomUUID().toString, true, Some(DateTime.now()), None)

        ES.indexImage(id, Json.toJson(imageWithBoringMetadata))
        eventually(timeout(fiveSeconds), interval(oneHundredMilliseconds))(reloadedImage(id).map(_.id) shouldBe Some(imageWithBoringMetadata.id))

        val updatedMetadata = Some(Edits(metadata = imageWithBoringMetadata.metadata.copy(description = Some("An interesting image"))))
        val updatedLastModifiedDate = DateTime.now

        Await.result(Future.sequence(
          ES.applyImageMetadataOverride(id,
            JsDefined(Json.toJson(updatedMetadata)),
            JsDefined(Json.toJson(updatedLastModifiedDate.toString)))),
          fiveSeconds)

        reloadedImage(id).flatMap(_.userMetadata.get.metadata.description) shouldBe Some("An interesting image")
      }

      "updating user metadata should update the image and user meta data last modified dates" in {
        val id = UUID.randomUUID().toString
        val imageWithBoringMetadata = createImageForSyndication(id = UUID.randomUUID().toString, true, Some(DateTime.now()), None)

        ES.indexImage(id, Json.toJson(imageWithBoringMetadata))
        eventually(timeout(fiveSeconds), interval(oneHundredMilliseconds))(reloadedImage(id).map(_.id) shouldBe Some(imageWithBoringMetadata.id))

        val updatedMetadata = Some(Edits(metadata = imageWithBoringMetadata.metadata.copy(description = Some("An updated image"))))
        val updatedLastModifiedDate = DateTime.now.withZone(DateTimeZone.UTC)

        Await.result(Future.sequence(
          ES.applyImageMetadataOverride(id,
            JsDefined(Json.toJson(updatedMetadata)),
            JsDefined(Json.toJson(updatedLastModifiedDate.toString)))),
          fiveSeconds)

        reloadedImage(id).flatMap(_.userMetadataLastModified) shouldEqual Some(updatedLastModifiedDate)
        reloadedImage(id).flatMap(_.lastModified) shouldEqual Some(updatedLastModifiedDate)
      }

      "original metadata is unchanged by a user metadata edit" in {
        val id = UUID.randomUUID().toString
        val imageWithBoringMetadata = createImageForSyndication(id = UUID.randomUUID().toString, true, Some(DateTime.now()), None)

        ES.indexImage(id, Json.toJson(imageWithBoringMetadata))
        eventually(timeout(fiveSeconds), interval(oneHundredMilliseconds))(reloadedImage(id).map(_.id) shouldBe Some(imageWithBoringMetadata.id))

        val updatedMetadata = Some(Edits(metadata = imageWithBoringMetadata.metadata.copy(description = Some("An interesting image"))))
        val updatedLastModifiedDate = DateTime.now

        Await.result(Future.sequence(
          ES.applyImageMetadataOverride(id,
            JsDefined(Json.toJson(updatedMetadata)),
            JsDefined(Json.toJson(updatedLastModifiedDate.toString)))),
          fiveSeconds)

        reloadedImage(id).map(_.originalMetadata) shouldEqual Some(imageWithBoringMetadata.originalMetadata)
      }

      "should ignore update if the proposed modification date is older than the current user metadata last modified date" in {
        val id = UUID.randomUUID().toString
        val imageWithBoringMetadata = createImageForSyndication(id = UUID.randomUUID().toString, true, Some(DateTime.now()), None)

        ES.indexImage(id, Json.toJson(imageWithBoringMetadata))
        eventually(timeout(fiveSeconds), interval(oneHundredMilliseconds))(reloadedImage(id).map(_.id) shouldBe Some(imageWithBoringMetadata.id))

        val latestMetadata = Some(Edits(metadata = imageWithBoringMetadata.metadata.copy(description = Some("Latest edit"))))
        val latestLastModifiedDate = DateTime.now.withZone(DateTimeZone.UTC)

        Await.result(Future.sequence(
          ES.applyImageMetadataOverride(id,
            JsDefined(Json.toJson(latestMetadata)),
            JsDefined(Json.toJson(latestLastModifiedDate.toString)))),
          fiveSeconds)

        val staleMetadata = Some(Edits(metadata = imageWithBoringMetadata.metadata.copy(description = Some("A stale edit"))))
        val staleLastModifiedDate = latestLastModifiedDate.minusSeconds(1)

        Await.result(Future.sequence(
          ES.applyImageMetadataOverride(id,
            JsDefined(Json.toJson(staleMetadata)),
            JsDefined(Json.toJson(staleLastModifiedDate.toString)))),
          fiveSeconds)

        reloadedImage(id).flatMap(_.userMetadata.get.metadata.description) shouldBe Some("Latest edit")
        reloadedImage(id).flatMap(_.userMetadataLastModified) shouldEqual Some(latestLastModifiedDate)
      }

      "updating user metadata with new usage rights should update usage rights" in {
        val id = UUID.randomUUID().toString
        val imageWithUsageRights = createImageForSyndication(id = UUID.randomUUID().toString, true, Some(DateTime.now()), None)

        ES.indexImage(id, Json.toJson(imageWithUsageRights))
        eventually(timeout(fiveSeconds), interval(oneHundredMilliseconds))(reloadedImage(id).map(_.id) shouldBe Some(imageWithUsageRights.id))

        val newPhotographer = StaffPhotographer(photographer = "Test Photographer", publication = "Testing")

        val metadataWithUpdatedUsageRights = Some(Edits(usageRights = Some(newPhotographer), metadata = imageWithUsageRights.metadata))

        Await.result(Future.sequence(
          ES.applyImageMetadataOverride(id,
            JsDefined(Json.toJson(metadataWithUpdatedUsageRights)),
            JsDefined(Json.toJson(DateTime.now.withZone(DateTimeZone.UTC).toString)))),
          fiveSeconds)

        reloadedImage(id).get.usageRights.asInstanceOf[StaffPhotographer].photographer shouldEqual("Test Photographer")
      }

      "???" - {

        /*
          // Script that refreshes the "metadata" object by recomputing it
  // from the original metadata and the overrides
  private val refreshMetadataScript =
  """| ctx._source.metadata = ctx._source.originalMetadata;
     | if (ctx._source.userMetadata && ctx._source.userMetadata.metadata) {
     |   ctx._source.metadata += ctx._source.userMetadata.metadata;
     |   // Get rid of "" values
     |   def nonEmptyKeys = ctx._source.metadata.findAll { it.value != "" }.collect { it.key }
     |   ctx._source.metadata = ctx._source.metadata.subMap(nonEmptyKeys);
     | }
  """.stripMargin

         */

      }
    }
  }

  private def reloadedImage(id: String) = Await.result(ES.getImage(id), fiveSeconds)

}