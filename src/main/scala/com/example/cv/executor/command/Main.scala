package com.example.cv.executor.command

import com.example.cv.CommandFactory

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Main {
  def main(args: Array[String]): Unit = {
    if (args.length < 2) {
      println("Usage: Main <commandName> <parameter1> [parameter2 ...]")
      sys.exit(1)
    }

    val commandName = args(0)
    val parameters = args.drop(1)

    val event = CommandFactory.create[Future](commandName, parameters).execute()
    println(event)
  }
}
