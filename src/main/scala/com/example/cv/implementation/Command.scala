package com.example.cv.implementation

import cats.Applicative
import cats.data.EitherT
import com.example.cv.design.Command.{NotifyCVRegistrationResultCommand, SaveApplyForCVRegistrationCommand, ValidateCVRegistrationCommand}
import com.example.cv.implementation.domain.CompoundModel.ValidatedApplyForCVRegistration
import com.example.cv.implementation.domain.Repository.SaveUnvalidatedApplyForCVRegistration
import com.example.cv.implementation.domain.TokenService.GenerateToken
import com.example.cv.implementation.domain.ValidatorService.{toValidatedBirthday, toValidatedMailAddress, toValidatedName}
import com.example.cv.implementation.domain.{DomainError, NotifiedCVRegistrationEvent, SavedApplyForCVRegistrationEvent, ValidatedCVRegistrationEvent}

object Command {
  type SaveApplyForCVRegistrationCommandImpl[F[_]] =
    SaveUnvalidatedApplyForCVRegistration[
      F
    ] => SaveApplyForCVRegistrationCommand[F]

  def saveApplyForCVRegistration[F[_]: Applicative]
      : SaveApplyForCVRegistrationCommandImpl[F] =
    saveUnvalidatedApplyForCVRegistration =>
      unvalidatedApplyForCVRegistration =>
        for {
          _ <- saveUnvalidatedApplyForCVRegistration(
            unvalidatedApplyForCVRegistration
          )
        } yield EitherT.rightT[F, DomainError](
          SavedApplyForCVRegistrationEvent(
            unvalidatedApplyForCVRegistration
          )
        )

  type ValidateCVRegistrationCommandImpl[F[_]] =
    ValidateCVRegistrationCommand[F]

  def validateCVRegistration[F[_]: Applicative]
      : ValidateCVRegistrationCommandImpl[F] =
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

  // FIXME: 本登録用URL（トークンパラメータつき）をメール送信
  type NotifyCVRegistrationResultCommandImpl[F[_]] =
    GenerateToken => NotifyCVRegistrationResultCommand[F]

  def notifyCVRegistrationResult[F[_]: Applicative]
      : NotifyCVRegistrationResultCommandImpl[F] =
    generateToken =>
      validatedApplyForCVRegistration => {
        val token = generateToken(32)
        EitherT.rightT[F, DomainError](
          NotifiedCVRegistrationEvent(
            validatedApplyForCVRegistration,
            token
          )
        )
      }
}
