package com.example.cv.implementation

import cats.Monad
import com.example.cv.design.Workflow.ApplyForCVRegistrationWorkflow
import com.example.cv.implementation.domain.cvregistration.{SavedApplyForCVRegistrationEvent, ValidatedCVRegistrationEvent}

object Workflow {

  def applyForCVRegistration[F[_]: Monad]: ApplyForCVRegistrationWorkflow[F] =
    (
      saveUnvalidatedApply,
      validate,
      notifyResult
    ) =>
      unvalidatedApply => {
        for {
          _ <- saveUnvalidatedApply(unvalidatedApply)
          validatedApply <- validate(unvalidatedApply)

          notifiedCVRegistrationEvent <-
            notifyResult(
              validatedApply
            )
        } yield {
          Seq(
            SavedApplyForCVRegistrationEvent(unvalidatedApply),
            ValidatedCVRegistrationEvent(validatedApply),
            notifiedCVRegistrationEvent
          )
        }
      }
}
