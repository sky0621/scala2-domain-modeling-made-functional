package com.example.cv2

object Main {
  def main(args: Array[String]): Unit = {
    val res = WorkflowA().execute()
    println(res)
  }
}

trait Event

case class EventA() extends Event
case class EventB() extends Event

trait Command {
  def execute(): Either[String, Event]
}

case class CommandA() extends Command {
  override def execute(): Either[String, Event] = {
    Either.cond(true, EventA(), "Error")
  }
}

case class CommandB() extends Command {
  override def execute(): Either[String, Event] = {
    Either.cond(true, EventB(), "Error")
  }
}

trait Workflow {
  def execute(): Either[String, Seq[Event]]
}

case class WorkflowA() extends Workflow {
  def execute(): Either[String, Seq[Event]] = {
    for {
      a <- CommandA().execute()
      b <- CommandB().execute()
    } yield {
      println(a)
      println(b)
      Seq(a, b)
    }
  }
}
