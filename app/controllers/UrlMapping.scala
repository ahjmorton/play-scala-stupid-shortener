package controllers

import play.api.data.format.Formatter
import play.api.data.FormError
import play.api.data.Forms
import play.api.data.Mapping

object UrlMapping {

  private def forceTransport(url:String) = {
    (if(url.startsWith("http://")) "" else "http://") + url
  }
  
  implicit val urlFormatter = new Formatter[String] {
    override def bind(key: String, data: Map[String, String]): Either[Seq[FormError], String] = {
      data.get(key).map { value => Right(forceTransport(value)) }
                   .getOrElse(error(key, "No contact type provided."))
    }

    private def error(key: String, msg: String) = Left(List(new FormError(key, msg)))

    override def unbind(key: String, value: String): Map[String, String] = {
      Map(key -> forceTransport(value.toString()))
    }
    
  }
  
  def url: Mapping[String] = Forms.of[String]
}