package com.example.cv.implementation

import cats.Applicative
import cats.data.EitherT
import com.example.cv.design.Command.{NotifyCVRegistrationResult, SaveApplyForCVRegistrationCommand, VerifyCVRegistrationCommand}
import com.example.cv.implementation.domain.DomainError

object Command {
  def saveApplyForCVRegistrationCommand[F[_]: Applicative]
      : SaveApplyForCVRegistrationCommand[F] =
    unvalidatedApplyForCVRegistration =>
      EitherT.rightT[F, DomainError](
        domain.SavedApplyForCVRegistrationEvent(
          unvalidatedApplyForCVRegistration
        )
      )

  def verifyCVRegistrationCommand[F[_]]: VerifyCVRegistrationCommand[F] =
    unvalidatedApplyForCVRegistration => ???

  def notifyCVRegistrationResult[F[_]]: NotifyCVRegistrationResult[F] =
    verifiedApplyForCVRegistration => ???
}
