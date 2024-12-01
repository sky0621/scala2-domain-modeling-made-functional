package com.example.cv

import cats.Monad
import cats.data.EitherT
import com.example.cv.Model.UnvalidatedMailAddress

import scala.concurrent.ExecutionContext

object WorkflowFactory {
  def create[F[_]: Monad](
      workflowName: String,
      parameters: Array[String]
  )(implicit
      ec: ExecutionContext
  ): Workflow[F] =
    workflowName match {
      case "applyForCVRegistration" =>
        ApplyForCVRegistrationWorkflow[F](
          UnvalidatedMailAddress(parameters.head)
        )
    }
}

trait Workflow[F[_]] {
  def execute(): EitherT[F, String, Seq[Event]]
}

case class ApplyForCVRegistrationWorkflow[F[_]: Monad](
    maybeMailAddress: UnvalidatedMailAddress
)(implicit ec: ExecutionContext)
    extends Workflow[F] {
  def execute(): EitherT[F, String, Seq[Event]] = {
    for {
      appliedForCVRegistrationEvent <-
        ApplyForCVRegistrationCommand[F](maybeMailAddress).execute()

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
      Seq(
        appliedForCVRegistrationEvent,
        verifiedCVRegistrationEvent,
        notifiedCVRegistrationEvent
      )
    }
  }
}
