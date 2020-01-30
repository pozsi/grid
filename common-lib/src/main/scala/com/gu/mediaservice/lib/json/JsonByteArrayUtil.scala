package com.gu.mediaservice.lib.json

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}
import java.nio.charset.StandardCharsets
import java.util.zip.{GZIPInputStream, GZIPOutputStream}

import play.api.Logger
import play.api.libs.json._

import scala.io.Source.fromInputStream
import scala.util.Try

object JsonByteArrayUtil extends PlayJsonHelpers {
  private val compressionMarkerByte: Byte = 0x00.toByte

  private def compress(bytes: Array[Byte]): Array[Byte] = {
    val outputStream = new ByteArrayOutputStream()
    val zipOutputStream = new GZIPOutputStream(outputStream)
    zipOutputStream.write(bytes)
    zipOutputStream.close()
    outputStream.close()
    val compressedBytes = outputStream.toByteArray
    compressionMarkerByte +: compressedBytes
  }

  private def decompress(bytes: Array[Byte]): Array[Byte] = {
    val bytesWithoutCompressionMarker = bytes.tail
    val byteStream = new ByteArrayInputStream(bytesWithoutCompressionMarker)
    val inputStream = new GZIPInputStream(byteStream)
    val decompressedBytes = fromInputStream(inputStream).mkString.getBytes
    byteStream.close()
    inputStream.close()
    decompressedBytes
  }

  def toByteArray[T](obj: T)(implicit writes: Writes[T]): Array[Byte] = compress(Json.toBytes(Json.toJson(obj)))

  def fromByteArray[T](bytes: Array[Byte])(implicit reads: Reads[T]): Option[T] = {
    val decompressedString = Try(new String(decompress(bytes), StandardCharsets.UTF_8)).toEither

    decompressedString match {
      case Left(value) => {
        Logger.error("unable to gunzip bytes", value)
        None
      }
      case Right(string) => {
        Json.parse(string).validate[T] match {
          case JsSuccess(obj, _) => Some(obj)
          case e: JsError => {
            logParseErrors(e)
            None
          }
        }
      }
    }
  }
}