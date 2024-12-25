package com.example.cv.implementation.domain.cvregistration

import cats.Applicative
import cats.data.EitherT
import com.example.cv.design.CVRegistration
import com.example.cv.implementation.domain.CompoundModel.ValidatedApplyForCVRegistration
import com.example.cv.implementation.domain.DomainError
import com.example.cv.implementation.domain.ValidatorService.{toValidatedBirthday, toValidatedMailAddress, toValidatedName}

object CVRegistrationValidator {
  type ValidateCVRegistration[F[_]] = CVRegistration.Validate[F]

  def validateCVRegistration[F[_]: Applicative]: ValidateCVRegistration[F] =
    unvalidatedApplyForCVRegistration =>
      EitherT.rightT[F, DomainError](
        ValidatedCVRegistrationEvent(
          ValidatedApplyForCVRegistration(
            toValidatedName(unvalidatedApplyForCVRegistration.unvalidatedName),
            toValidatedBirthday(
              unvalidatedApplyForCVRegistration.unvalidatedBirthday
            ),
            toValidatedMailAddress(
              unvalidatedApplyForCVRegistration.unvalidatedMailAddress
            )
          )
        )
      )
}
