package com.example.cv.domain

import cats.Applicative
import com.example.cv.domain.MailAddress.{InvalidMailAddress, UnvalidatedMailAddress, ValidatedMailAddress}

import scala.concurrent.ExecutionContext

object CommandFactory {
  def create[F[_]: Applicative](
      commandName: String,
      parameters: Array[String]
  )(implicit ec: ExecutionContext): Command[F, _] =
    commandName match {
      case "applyForCVRegistration" =>
        ApplyForCVRegistrationCommand[F](
          UnvalidatedMailAddress(parameters.head)
        )
      case "verifyCVRegistration" =>
        VerifyCVRegistrationCommand[F](UnvalidatedMailAddress(parameters.head))
      case "notifyApprovedCVRegistrationResult" =>
        NotifyApprovedCVRegistrationResultCommand[F](
          ValidatedMailAddress(parameters.head)
        )
      case "notifyRejectedCVRegistrationResult" =>
        NotifyRejectedCVRegistrationResultCommand[F](
          InvalidMailAddress(parameters.head)
        )
      case _ => throw new RuntimeException()
    }
}
