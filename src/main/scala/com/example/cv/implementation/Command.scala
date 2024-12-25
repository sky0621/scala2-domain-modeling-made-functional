package com.example.cv.implementation

import cats.Applicative
import cats.data.EitherT
import com.example.cv.design.Service.GenerateToken
import com.example.cv.implementation.domain.DomainError
import com.example.cv.implementation.domain.Model.TokenLength

object Command {

  // FIXME: 本登録用URL（トークンパラメータつき）をメール送信
  type NotifyCVRegistrationResultCommandImpl[F[_]] =
    GenerateToken => NotifyCVRegistrationResultCommand[F]

  def notifyCVRegistrationResult[
    F[_]: Applicative
  ]: NotifyCVRegistrationResultCommandImpl[F] =
    generateToken =>
      validatedApplyForCVRegistration => {
        val token = generateToken(TokenLength(32))
        EitherT.rightT[F, DomainError](
          NotifiedCVRegistrationEvent(
            validatedApplyForCVRegistration,
            token
          )
        )
      }
}
