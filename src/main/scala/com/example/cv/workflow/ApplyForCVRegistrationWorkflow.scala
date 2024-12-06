package com.example.cv.workflow

import cats.Monad
import cats.data.EitherT
import com.example.cv.domain.MailAddress.UnvalidatedMailAddress
import com.example.cv.domain._

import scala.concurrent.ExecutionContext

// FIXME: ec を除去！（ ec を隠蔽する関数を外から渡す方式にする）
class ApplyForCVRegistrationWorkflow[
    F[_]: Monad
](implicit
    ec: ExecutionContext
) extends Workflow[
      F,
      ApplyForCVRegistrationInputDto,
      DomainError,
      ApplyForCVRegistrationOutputDto
    ] {
  def execute(
      input: ApplyForCVRegistrationInputDto
  ): EitherT[F, DomainError, ApplyForCVRegistrationOutputDto] = {
    for {
      appliedForCVRegistrationEvent <-
        ApplyForCVRegistrationCommand[F](input.maybeMailAddress).execute()

      verifiedCVRegistrationEvent <-
        VerifyCVRegistrationCommand[F](
          appliedForCVRegistrationEvent.maybeMailAddress
        ).execute()

      notifiedCVRegistrationEvent <- verifiedCVRegistrationEvent match {
        case ApprovedCVRegistrationEvent(validatedMailAddress) =>
          NotifyApprovedCVRegistrationResultCommand[F](
            validatedMailAddress
          )
            .execute()
        case RejectedCVRegistrationEvent(invalidMailAddress) =>
          NotifyRejectedCVRegistrationResultCommand[F](invalidMailAddress)
            .execute()
      }
    } yield {
      ApplyForCVRegistrationOutputDto(
        Seq(
          appliedForCVRegistrationEvent,
          verifiedCVRegistrationEvent,
          notifiedCVRegistrationEvent
        )
      )
    }
  }
}

case class ApplyForCVRegistrationInputDto(
    maybeMailAddress: UnvalidatedMailAddress
) extends InputDto
object ApplyForCVRegistrationInputDto {
  def apply(value: String): ApplyForCVRegistrationInputDto =
    new ApplyForCVRegistrationInputDto(UnvalidatedMailAddress(value))
}

case class ApplyForCVRegistrationOutputDto(events: Seq[Event]) extends OutputDto
