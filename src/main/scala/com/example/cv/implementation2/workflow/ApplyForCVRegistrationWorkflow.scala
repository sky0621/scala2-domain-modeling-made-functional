package com.example.cv.implementation2.workflow

import cats.Monad
import cats.data.EitherT
import com.example.cv.implementation2.domain.Birthday.UnvalidatedBirthday
import com.example.cv.implementation2.domain.MailAddress.UnvalidatedMailAddress
import com.example.cv.implementation2.domain.Name.UnvalidatedName
import com.example.cv.implementation2.domain._

import scala.concurrent.ExecutionContext

object ApplyForCVRegistrationWorkflow {
  type ExecuteApplyForCVRegistrationWorkflow = ???
}

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
        ApplyForCVRegistrationCommand[F].execute(
          input.maybeName,
          input.maybeBirthday,
          input.maybeMailAddress
        )

      verifiedCVRegistrationEvent <-
        VerifyCVRegistrationCommandTrait[F](
          appliedForCVRegistrationEvent.maybeMailAddress
        ).execute()

      notifiedCVRegistrationEvent <- verifiedCVRegistrationEvent match {
        case ApprovedCVRegistrationEvent(validatedMailAddress) =>
          NotifyApprovedCVRegistrationResultCommandTrait[F](
            validatedMailAddress
          )
            .execute()
        case RejectedCVRegistrationEvent(invalidMailAddress) =>
          NotifyRejectedCVRegistrationResultCommandTrait[F](invalidMailAddress)
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
    maybeName: UnvalidatedName,
    maybeBirthday: UnvalidatedBirthday,
    maybeMailAddress: UnvalidatedMailAddress
) extends InputDto

case class ApplyForCVRegistrationOutputDto(events: Seq[Event]) extends OutputDto
