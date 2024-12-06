package com.example.cv.domain

import cats.Applicative
import com.example.cv.domain.Birthday.UnvalidatedBirthday
import com.example.cv.domain.MailAddress.{InvalidMailAddress, UnvalidatedMailAddress, ValidatedMailAddress}
import com.example.cv.domain.Name.UnvalidatedName

import scala.concurrent.ExecutionContext

object CommandFactory {
  def create[F[_]: Applicative](
      commandName: String,
      parameters: Array[String]
  )(implicit ec: ExecutionContext): Command[F, _] =
    commandName match {
      case "applyForCVRegistration" =>
        ApplyForCVRegistrationCommand[F](
          UnvalidatedName(parameters(1), parameters.head),
          UnvalidatedBirthday(
            parameters(2).toInt,
            parameters(3).toInt,
            parameters(4).toInt
          ),
          UnvalidatedMailAddress(parameters(5))
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
