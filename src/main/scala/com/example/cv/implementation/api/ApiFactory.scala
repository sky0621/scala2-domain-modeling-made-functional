package com.example.cv.implementation.api

import cats.Monad
import com.example.cv.implementation.Command.notifyCVRegistrationResult
import com.example.cv.implementation.domain.TokenService
import com.example.cv.implementation.domain.cvregistration.CVRegistrationValidator
import com.example.cv.implementation.infra.CVRegistrationInfra

object ApiFactory {

  def create[F[_]: Monad](
    apiName: String
  ): Api[F] = apiName match {
    case "applyForCVRegistration" =>
      new ApplyForCVRegistrationApi(
        CVRegistrationInfra.saveUnvalidatedApplyForCVRegistration,
        CVRegistrationValidator.validateCVRegistration,
        notifyCVRegistrationResult[F],
        TokenService.generateToken
      ).asInstanceOf[Api[F]]
    case _ => throw new IllegalArgumentException(apiName)
  }
}
