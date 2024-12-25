package com.example.cv.implementation.infra

import cats.data.EitherT
import cats.implicits._
import com.example.cv.design.CVRegistration
import com.example.cv.implementation.domain.DomainError

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

object CVRegistrationInfra {
  type SaveUnvalidatedApply[F[_]] =
    ExecutionContext => CVRegistration.SaveUnvalidatedApply[F]

  def saveUnvalidatedApplyForCVRegistration: SaveUnvalidatedApply[Future] = _ =>
    _ => EitherT.pure[Future, DomainError]()
}
