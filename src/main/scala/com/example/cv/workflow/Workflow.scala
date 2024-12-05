package com.example.cv.workflow

import cats.Monad
import cats.data.EitherT
import com.example.cv.domain.Model.UnvalidatedMailAddress
import com.example.cv.domain._

import scala.concurrent.ExecutionContext

trait Workflow[F[_], I <: Input] {
  def execute(input: I): EitherT[F, String, Output]
}

// FIXME: ec を除去！（ ec を隠蔽する関数を外から渡す方式にする）
// FIXME: Left は String でなく専用のエラークラスを検討！
class ApplyForCVRegistrationWorkflow[
    F[_]: Monad
](implicit
    ec: ExecutionContext
) extends Workflow[F, ApplyForCVRegistrationInput] {
  def execute(
      input: ApplyForCVRegistrationInput
  ): EitherT[F, String, Output] = {
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
      ApplyForCVRegistrationOutput(
        Seq(
          appliedForCVRegistrationEvent,
          verifiedCVRegistrationEvent,
          notifiedCVRegistrationEvent
        )
      )
    }
  }
}

trait Input

case class ApplyForCVRegistrationInput(
    maybeMailAddress: UnvalidatedMailAddress
) extends Input

trait Output

case class ApplyForCVRegistrationOutput(events: Seq[Event]) extends Output
