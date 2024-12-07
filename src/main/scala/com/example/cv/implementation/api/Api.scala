package com.example.cv.implementation.api

import cats.Monad
import cats.data.EitherT

trait Api[F[_]] {
  def execute(
      request: Request
  )(implicit monad: Monad[F]): EitherT[F, ApiError, Response]
}

case class Request(values: Array[String])

trait Response
