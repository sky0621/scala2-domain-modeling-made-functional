package com.example.cv
import cats.Applicative
import cats.data.EitherT
import com.example.cv.Model._

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

trait Command[F[_], E <: Event] {
  def execute(): EitherT[F, String, E]
}

case class ApplyForCVRegistrationCommand[F[_]: Applicative](
    maybeMailAddress: UnvalidatedMailAddress
)(implicit ec: ExecutionContext)
    extends Command[F, AppliedForCVRegistrationEvent] {
  override def execute(): EitherT[F, String, AppliedForCVRegistrationEvent] = {
    val mailOpt = Option.when(maybeMailAddress.value.nonEmpty)(maybeMailAddress)
    for {
      _ <- InMemoryDatabase.unvalidatedMailAddressStorage[F].save(mailOpt)
    } yield AppliedForCVRegistrationEvent(maybeMailAddress)
  }
}

case class VerifyCVRegistrationCommand[F[_]: Applicative](
    maybeMailAddress: UnvalidatedMailAddress
)(implicit ec: ExecutionContext)
    extends Command[F, VerifiedCVRegistrationEvent] {

  override def execute(): EitherT[F, String, VerifiedCVRegistrationEvent] =
    EitherT.rightT[F, String] {
      if (MailAddress.is(maybeMailAddress)) {
        ApprovedCVRegistrationEvent(
          ValidatedMailAddress(maybeMailAddress.value)
        )
      } else {
        RejectedCVRegistrationEvent(
          InvalidMailAddress(maybeMailAddress.value)
        )
      }
    }
}

case class NotifyApprovedCVRegistrationResultCommand[F[_]: Applicative](
    validatedMailAddress: ValidatedMailAddress
)(implicit ec: ExecutionContext)
    extends Command[F, NotifiedApprovedCVRegistrationEvent] {

  override def execute()
      : EitherT[F, String, NotifiedApprovedCVRegistrationEvent] =
    EitherT.rightT[F, String] {
      NotifiedApprovedCVRegistrationEvent(
        validatedMailAddress,
        ApprovedCVRegistrationMessage("CV registration application approved")
      )
    }
}

case class NotifyRejectedCVRegistrationResultCommand[F[_]: Applicative](
    invalidMailAddress: InvalidMailAddress
)(implicit ec: ExecutionContext)
    extends Command[F, NotifiedRejectedCVRegistrationEvent] {

  override def execute()
      : EitherT[F, String, NotifiedRejectedCVRegistrationEvent] =
    EitherT.rightT[F, String] {
      NotifiedRejectedCVRegistrationEvent(
        invalidMailAddress,
        RejectedCVRegistrationMessage("CV registration application rejected")
      )
    }
}
