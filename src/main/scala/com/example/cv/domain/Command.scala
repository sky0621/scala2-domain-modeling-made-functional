package com.example.cv.domain

import cats.Applicative
import cats.data.EitherT
import com.example.cv.InMemoryDatabase
import com.example.cv.domain.Model._

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

// CV登録を申請する
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

// CV登録申請内容を検証する
case class VerifyCVRegistrationCommand[F[_]: Applicative](
    maybeMailAddress: UnvalidatedMailAddress
)(implicit ec: ExecutionContext)
    extends Command[F, VerifiedCVRegistrationEvent] {

  override def execute(): EitherT[F, String, VerifiedCVRegistrationEvent] = {
    if (MailAddress.is(maybeMailAddress)) {
      EitherT.rightT[F, String] {
        ApprovedCVRegistrationEvent(
          ValidatedMailAddress(maybeMailAddress.value)
        )
      }
    } else {
      EitherT.rightT[F, String] {
        RejectedCVRegistrationEvent(
          InvalidMailAddress(maybeMailAddress.value)
        )
      }
    }
  }
}

// CV登録申請承認結果を通知する
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

// CV登録申請拒否結果を通知する
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
