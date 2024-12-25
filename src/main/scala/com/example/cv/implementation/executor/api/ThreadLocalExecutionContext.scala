package com.example.cv.implementation.executor.api

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext

object ThreadLocalExecutionContext {
  private val threadLocalExecutionContext = new ThreadLocal[ExecutionContext] {
    override def initialValue(): ExecutionContext = {
      val threadPool = Executors.newFixedThreadPool(2)
      ExecutionContext.fromExecutor(threadPool)
    }
  }

  def get: ExecutionContext = threadLocalExecutionContext.get()
}
