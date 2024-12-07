package com.example.cv.implementation2.api

import cats.data.EitherT

import scala.concurrent.ExecutionContext

trait Api[F[_], E <: ApiError, R <: Response] {
  def execute(request: Request)(implicit ec: ExecutionContext): EitherT[F, E, R]
}
