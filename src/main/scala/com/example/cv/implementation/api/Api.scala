package com.example.cv.implementation.api

import cats.Monad
import cats.data.EitherT

import scala.concurrent.ExecutionContext

trait Api[F[_]] {

  def execute(
    request: Request
  )(
    implicit
    monad: Monad[F],
    ec: ExecutionContext
  ): EitherT[F, ApiError, Response]
}

case class Request(values: Array[String])

trait Response {
  def show(): Unit
}
