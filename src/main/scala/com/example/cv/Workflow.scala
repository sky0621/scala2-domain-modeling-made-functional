package com.example.cv

import cats.data.EitherT
import com.example.cv.Model.UnvalidatedMailAddress

import scala.concurrent.{ExecutionContext, Future}

object WorkflowFactory {
  def create(
      workflowName: String,
      parameters: Array[String]
  )(implicit
      ec: ExecutionContext
  ): Workflow =
    workflowName match {
      case "applyForCVRegistration" =>
        ApplyForCVRegistrationWorkflow(
          UnvalidatedMailAddress(parameters.head)
        )
    }
}

trait Workflow {
  def execute(): EitherT[Future, String, Seq[Event]]
}

case class ApplyForCVRegistrationWorkflow(
    maybeMailAddress: UnvalidatedMailAddress
)(implicit ec: ExecutionContext)
    extends Workflow {
  def execute(): EitherT[Future, String, Seq[Event]] = {
    for {
      appliedForCVRegistrationEvent <-
        ApplyForCVRegistrationCommand[Future](maybeMailAddress).execute()
      _ <- EitherT.rightT[Future, String] {
        println(appliedForCVRegistrationEvent)
      }

      verifiedCVRegistrationEvent <-
        ApproveCVRegistrationCommand[Future](
          appliedForCVRegistrationEvent.maybeMailAddress
        ).execute()

//      notifiedCVRegistrationEvent <- verifiedCVRegistrationEvent match {
//        case ApprovedCVRegistrationEvent(validatedMailAddress) =>
//          NotifyApprovedCVRegistrationResultCommand[Future](
//            validatedMailAddress
//          )
//            .execute()
//        case RejectedCVRegistrationEvent(invalidMailAddress) =>
//          NotifyRejectedCVRegistrationResultCommand[Future](invalidMailAddress)
//            .execute()
//      }
    } yield {
      Seq(
        appliedForCVRegistrationEvent,
        verifiedCVRegistrationEvent
//        notifiedCVRegistrationEvent
      )
    }
  }
}
