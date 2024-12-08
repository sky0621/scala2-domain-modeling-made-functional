package com.example.cv.implementation

import cats.Applicative
import cats.data.EitherT
import com.example.cv.design.Command.{NotifyCVRegistrationResult, SaveApplyForCVRegistration, ValidateCVRegistration}
import com.example.cv.implementation.domain.CompoundModel.ValidatedApplyForCVRegistration
import com.example.cv.implementation.domain.TokenService.GenerateToken
import com.example.cv.implementation.domain.ValidatorService.{toValidatedBirthday, toValidatedMailAddress, toValidatedName}
import com.example.cv.implementation.domain.{DomainError, NotifiedCVRegistrationEvent, ValidatedCVRegistrationEvent}

object Command {
  // FIXME: CV申し込み情報の永続化
  type SaveApplyForCVRegistrationImpl[F[_]] = SaveApplyForCVRegistration[F]
  def saveApplyForCVRegistration[F[_]: Applicative]
      : SaveApplyForCVRegistrationImpl[F] =
    unvalidatedApplyForCVRegistration =>
      EitherT.rightT[F, DomainError](
        domain.SavedApplyForCVRegistrationEvent(
          unvalidatedApplyForCVRegistration
        )
      )

  type ValidateCVRegistrationImpl[F[_]] = ValidateCVRegistration[F]
  def validateCVRegistration[F[_]: Applicative]: ValidateCVRegistrationImpl[F] =
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
  type NotifyCVRegistrationResultImpl[F[_]] =
    GenerateToken => NotifyCVRegistrationResult[F]
  def notifyCVRegistrationResult[F[_]: Applicative]
      : NotifyCVRegistrationResultImpl[F] =
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
