package com.example.cv.domain

import cats.Applicative
import cats.data.EitherT
import com.example.cv.InMemoryDatabase
import com.example.cv.domain.Birthday.UnvalidatedBirthday
import com.example.cv.domain.MailAddress.{InvalidMailAddress, UnvalidatedMailAddress, ValidatedMailAddress}
import com.example.cv.domain.Message._
import com.example.cv.domain.Name.UnvalidatedName

import scala.concurrent.ExecutionContext

trait Command[F[_], E <: Event] {
  def execute(): EitherT[F, DomainError, E]
}

// CV登録を申請する
case class ApplyForCVRegistrationCommand[F[_]: Applicative](
    maybeName: UnvalidatedName,
    maybeBirthday: UnvalidatedBirthday,
    maybeMailAddress: UnvalidatedMailAddress
)(implicit ec: ExecutionContext)
    extends Command[F, AppliedForCVRegistrationEvent] {
  override def execute()
      : EitherT[F, DomainError, AppliedForCVRegistrationEvent] = {
    val mailOpt = Option.when(maybeMailAddress.value.nonEmpty)(maybeMailAddress)
    for {
      _ <- InMemoryDatabase
        .unvalidatedMailAddressStorage[F]
        .save(mailOpt)
        .leftMap[DomainError](e => UnexpectedError(e))
    } yield AppliedForCVRegistrationEvent(maybeMailAddress)
  }
}

// CV登録申請内容を検証する
case class VerifyCVRegistrationCommand[F[_]: Applicative](
    maybeMailAddress: UnvalidatedMailAddress
)(implicit ec: ExecutionContext)
    extends Command[F, VerifiedCVRegistrationEvent] {

  override def execute()
      : EitherT[F, DomainError, VerifiedCVRegistrationEvent] = {
    if (MailAddress.is(maybeMailAddress)) {
      EitherT.rightT[F, DomainError] {
        ApprovedCVRegistrationEvent(
          ValidatedMailAddress(maybeMailAddress.value)
        )
      }
    } else {
      EitherT.rightT[F, DomainError] {
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
      : EitherT[F, DomainError, NotifiedApprovedCVRegistrationEvent] =
    EitherT.rightT[F, DomainError] {
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
      : EitherT[F, DomainError, NotifiedRejectedCVRegistrationEvent] =
    EitherT.rightT[F, DomainError] {
      NotifiedRejectedCVRegistrationEvent(
        invalidMailAddress,
        RejectedCVRegistrationMessage("CV registration application rejected")
      )
    }
}
