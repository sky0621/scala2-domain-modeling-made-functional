package com.example.cv.implementation.executor.api

import com.example.cv.implementation.api.{ApiFactory, Request}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, ExecutionContext, Future}

object Main {
  def main(args: Array[String]): Unit = {
    if (args.length < 1) {
      println("Usage: Main <commandName> [<parameter1, parameter2 ...]")
      sys.exit(1)
    }
    val apiName = args(0)
    val parameters = args.drop(1)

    val ec: ExecutionContext = ThreadLocalExecutionContext.get

    val api = ApiFactory.create[Future](apiName)
    val maybeResponse = api.execute(Request(parameters)).value
    try {
      val eitherResponse = Await.result(maybeResponse, Duration.Inf)
      eitherResponse match {
        case Left(error)  => println(s"Error: $error")
        case Right(value) => value.show()
      }
    } catch {
      case ex: Exception => println(s"Exception occurred: ${ex.getMessage}")
    }
  }
}
