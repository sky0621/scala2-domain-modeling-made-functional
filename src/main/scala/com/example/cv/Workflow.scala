package com.example.cv

import cats.Monad
import cats.data.EitherT
import com.example.cv.Model.UnvalidatedMailAddress

import scala.concurrent.ExecutionContext

object WorkflowFactory {
  def create[F[_]: Monad, E <: Event](
      workflowName: String,
      parameters: Array[String]
  )(implicit
      ec: ExecutionContext
  ): Workflow[F, E] =
    workflowName match {
      case "applyForCVRegistration" =>
        ApplyForCVRegistrationWorkflow[F, E](
          UnvalidatedMailAddress(parameters.head)
        )
    }
}

trait Workflow[F[_], E <: Event] {
  def execute(): EitherT[F, String, Seq[E]]
}

case class ApplyForCVRegistrationWorkflow[F[_]: Monad, E <: Event](
    maybeMailAddress: UnvalidatedMailAddress
)(implicit ec: ExecutionContext)
    extends Workflow[F, E] {
  def execute(): EitherT[F, String, Seq[E]] = {
    for {
      appliedForCVRegistrationEvent <-
        ApplyForCVRegistrationCommand[F](maybeMailAddress).execute()

      verifiedCVRegistrationEvent <-
        VerifyCVRegistrationCommand[F](
          appliedForCVRegistrationEvent.maybeMailAddress
        ).execute()

      notifiedCVRegistrationEvent <- verifiedCVRegistrationEvent match {
        case ApprovedCVRegistrationEvent(validatedMailAddress) =>
          NotifyApprovedCVRegistrationResultCommand[F](validatedMailAddress)
            .execute()
        case RejectedCVRegistrationEvent(invalidMailAddress) =>
          NotifyRejectedCVRegistrationResultCommand[F](invalidMailAddress)
            .execute()
      }
    } yield {
      Seq(
        appliedForCVRegistrationEvent.asInstanceOf[E],
        verifiedCVRegistrationEvent.asInstanceOf[E],
        notifiedCVRegistrationEvent.asInstanceOf[E]
      )
    }
  }
}
