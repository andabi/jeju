package controllers

import anorm.{Pk, NotAssigned}
import org.joda.time.DateTime
import org.joda.time.format.ISODateTimeFormat
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import play.api.libs.json._
import models.Message

object MessageApi extends Controller {

  implicit val messageWrites = new Writes[Message] {
    def writes(message: Message) = Json.obj(
      "writer" -> message.writer,
      "content" -> message.content,
      "created_at" -> message.created_at.toString(ISODateTimeFormat.dateTime)
    )
  }

  def get(id: Long) = Action { implicit request =>
    Message.findById(id) match {
      case Some(message) => Ok(Json.toJson(message))
      case None => Ok("not found")
    }
  }

  def list = Action { implicit request =>

    case class FormData(after: Option[DateTime])

    val form = Form(
      mapping(
        "after" -> optional(jodaDate("yyyy-MM-dd'T'HH:mm:ss.sssZZ"))
      )(FormData.apply)(FormData.unapply)
    )

    form.bindFromRequest.fold(
      formWithErrors => BadRequest,
      form => {
        val list = form.after match {
          case Some(time) => Message.listAfter(time)
          case None => Message.list
        }
        Ok(Json.toJson(list))
      }
    )
  }


  def save = Action { implicit request =>

    val form = Form(
      mapping(
        "id" -> ignored(NotAssigned:Pk[Long]),
        "writer" -> text,
        "content" -> text,
        "created_at" -> ignored(new DateTime)
      )(Message.apply)(Message.unapply)
    )

    form.bindFromRequest.fold(
      formWithErrors => BadRequest,
      message => {
        Message.insert(message)
        Ok
      }
    )
  }
}