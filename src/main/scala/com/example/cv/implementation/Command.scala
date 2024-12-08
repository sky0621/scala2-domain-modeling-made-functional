package com.example.cv.implementation

import cats.Applicative
import cats.data.EitherT
import com.example.cv.design.Command.{NotifyCVRegistrationResult, SaveApplyForCVRegistrationCommand, VerifyCVRegistrationCommand}
import com.example.cv.implementation.domain.CompoundModel.ValidatedApplyForCVRegistration
import com.example.cv.implementation.domain.ValidatorService.{toValidatedBirthday, toValidatedMailAddress, toValidatedName}
import com.example.cv.implementation.domain.{DomainError, NotifiedCVRegistrationEvent, TokenService, ValidatedCVRegistrationEvent}

object Command {
  // FIXME: CV申し込み情報の永続化
  def saveApplyForCVRegistrationCommand[F[_]: Applicative]
      : SaveApplyForCVRegistrationCommand[F] =
    unvalidatedApplyForCVRegistration =>
      EitherT.rightT[F, DomainError](
        domain.SavedApplyForCVRegistrationEvent(
          unvalidatedApplyForCVRegistration
        )
      )

  def verifyCVRegistrationCommand[F[_]: Applicative]
      : VerifyCVRegistrationCommand[F] =
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
  // FIXME: トークンジェネレーターを依存で渡す
  def notifyCVRegistrationResult[F[_]: Applicative]
      : NotifyCVRegistrationResult[F] =
    validatedApplyForCVRegistration => {
      val token = TokenService.generateToken
      EitherT.rightT[F, DomainError](
        NotifiedCVRegistrationEvent(
          validatedApplyForCVRegistration,
          token
        )
      )
    }
}
