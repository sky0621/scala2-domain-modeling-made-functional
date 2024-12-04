package com.example.cv.executor.api

import com.example.cv.api.{ApiFactory, Request}

object Main {
  def main(args: Array[String]): Unit = {
    if (args.length < 1) {
      println("Usage: Main <commandName> [<parameter1, parameter2 ...]")
      sys.exit(1)
    }
    val apiName = args(0)
    val parameters = args.drop(1)

    val api = ApiFactory.create(apiName)
    val response = api.execute(Request(parameters))
    println(response)
  }
}
