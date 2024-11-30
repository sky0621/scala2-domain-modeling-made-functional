package com.example.cv.executor.workflow

import com.example.cv.WorkflowFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

object Main {
  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("Usage: Main <commandName> <parameter1> [parameter2 ...]")
      sys.exit(1)
    }

    val workflowName = args(0)
    val parameters = args.drop(1)

    val workflow =
      WorkflowFactory.create(workflowName, parameters)
    workflow.execute().value.onComplete {
      case Success(Right(events)) =>
        println(s"Workflow succeeded: $events")
      case Success(Left(error)) =>
        println(s"Workflow failed with error: $error")
      case Failure(exception) =>
        println(s"Workflow execution exception: $exception")
    }
  }
}
