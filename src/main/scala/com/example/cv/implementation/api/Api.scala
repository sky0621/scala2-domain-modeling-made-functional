package com.example.cv.implementation.api

import cats.Monad
import cats.data.EitherT

trait Api[F[_], R <: Response] {
  def execute(
      request: Request
  )(implicit monad: Monad[F]): EitherT[F, ApiError, R]
}

case class Request(values: Array[String])

trait Response
