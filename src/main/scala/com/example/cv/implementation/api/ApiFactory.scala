package com.example.cv.implementation.api

import cats.Monad
import com.example.cv.implementation.Command.{notifyCVRegistrationResult, saveApplyForCVRegistration, validateCVRegistration}
import com.example.cv.implementation.domain.TokenService

object ApiFactory {
  def create[F[_]: Monad](
      apiName: String
  ): Api[F] = apiName match {
    case "applyForCVRegistration" =>
      new ApplyForCVRegistrationApi[F](
        saveApplyForCVRegistration[F],
        validateCVRegistration[F],
        notifyCVRegistrationResult[F],
        TokenService.generateToken
      ).asInstanceOf[Api[F]]
    case _ => throw new IllegalArgumentException(apiName)
  }
}
