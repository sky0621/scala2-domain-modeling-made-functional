package com.example.cv.executor.workflow

import com.example.cv.{Event, WorkflowFactory}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Main {
  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("Usage: Main <commandName> <parameter1> [parameter2 ...]")
      sys.exit(1)
    }

    val workflowName = args(0)
    val parameters = args.drop(1)

    for {
      events <- WorkflowFactory
        .create[Future, Event](workflowName, parameters)
        .execute()
    } yield {
      println(events)
    }
  }
}
