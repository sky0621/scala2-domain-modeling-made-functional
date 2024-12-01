package com.example.cv.executor.workflow

import com.example.cv.WorkflowFactory

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.DurationInt

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

    for {
      results <- Await.result(workflow.execute().value, 3.seconds)
    } yield {
      results.foreach(println)
    }
  }
}
