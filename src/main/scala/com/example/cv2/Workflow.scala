package com.example.cv2

import cats.data.EitherT
import cats.{Applicative, Id, Monad}

object Main {
  def main(args: Array[String]): Unit = {
    val res = WorkflowA[Id]().execute()
    println(res)
  }
}

trait Event

case class EventA() extends Event
case class EventB() extends Event

trait Command[F[_]] {
  def execute(): EitherT[F, String, Event]
}

case class CommandA[F[_]: Applicative]() extends Command[F] {
  override def execute(): EitherT[F, String, Event] = {
    EitherT.cond[F](test = true, EventA(), "Error")
  }
}

case class CommandB[F[_]: Applicative]() extends Command[F] {
  override def execute(): EitherT[F, String, Event] = {
    EitherT.cond[F](test = true, EventB(), "Error")
  }
}

trait Workflow[F[_]] {
  def execute(): EitherT[F, String, Seq[Event]]
}

case class WorkflowA[F[_]: Monad]() extends Workflow[F] {
  def execute(): EitherT[F, String, Seq[Event]] = {
    for {
      a <- CommandA[F]().execute()
      b <- CommandB[F]().execute()
    } yield {
      println(a)
      println(b)
      Seq(a, b)
    }
  }
}
