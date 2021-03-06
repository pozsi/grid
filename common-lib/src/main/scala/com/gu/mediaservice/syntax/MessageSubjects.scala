package com.gu.mediaservice.syntax

trait MessageSubjects {

  val Image = "image"
  val ReingestImage = "reingest-image"
  val DeleteImage = "delete-image"
  val UpdateImage = "update-image"
  val DeleteImageExports = "delete-image-exports"
  val UpdateImageExports = "update-image-exports"
  val UpdateImageUserMetadata = "update-image-user-metadata"
  val UpdateImageUsages = "update-image-usages"
  val ReplaceImageLeases = "replace-image-leases"
  val AddImageLease = "add-image-lease"
  val RemoveImageLease = "remove-image-lease"
  val SetImageCollections = "set-image-collections"
  val DeleteUsages = "delete-usages"
  // TODO This will be removed once RCS poller lambda talks to Edits subsystem.
  val UpsertRcsRights = "upsert-rcs-rights"
  val UpdateImageSyndicationMetadata = "update-image-syndication-metadata"
  // TODO This will be removed once RCS poller lambda talks to Edits subsystem.
  val UpdateImagePhotoshoot = "update-image-photoshoot"
  val UpdateImagePhotoshootMetadata = "update-image-photoshoot-metadata"

}

object MessageSubjects extends MessageSubjects
