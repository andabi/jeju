package models

import org.joda.time.DateTime
import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

import scala.language.postfixOps

case class Message(id: Pk[Long] = NotAssigned, writer: String, content: String, created_at: DateTime)

object Message {
  
  val simple = {
    get[Pk[Long]]("message.id") ~
    get[String]("message.writer") ~
    get[String]("message.content") ~
    get[DateTime]("message.created_at") map {
      case id~writer~content~created_at => Message(id, writer, content, created_at)
    }
  }
  
  def findById(id: Long): Option[Message] = {
    DB.withConnection { implicit connection =>
      SQL("select * from message where id = {id}").on('id -> id).as(Message.simple.singleOpt)
    }
  }

  def list: List[Message] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select * from message
          order by created_at desc
        """
      ).as(Message.simple *)
    }
  }

  def listAfter(time: DateTime): List[Message] = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          select * from message
          where created_at > {time}
          order by created_at desc
        """
      ).on(
        'time -> time
      ).as(Message.simple *)
    }
  }

  def insert(message: Message) = {
    DB.withConnection { implicit connection =>
      SQL(
        """
          insert into message values (
            null, {writer}, {content}, now()
          )
        """
      ).on(
        'writer -> message.writer,
        'content -> message.content
      ).executeUpdate()
    }
  }
}

