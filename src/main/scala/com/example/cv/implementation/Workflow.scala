package com.example.cv.implementation

import cats.Monad
import com.example.cv.design.Design.ApplyForCVRegistrationWorkflow
import com.example.cv.implementation.domain.Birthday.UnvalidatedBirthday
import com.example.cv.implementation.domain.CompoundModel.UnvalidatedApplyForCVRegistration
import com.example.cv.implementation.domain.Event
import com.example.cv.implementation.domain.MailAddress.UnvalidatedMailAddress
import com.example.cv.implementation.domain.Name.UnvalidatedName

object Workflow {
  def applyForCVRegistration[F[_]: Monad]: ApplyForCVRegistrationWorkflow[F] =
    saveApplyForCVRegistrationCommand =>
      verifyCVRegistrationCommand =>
        notifyCVRegistrationResult =>
          input =>
            for {
              savedApplyForCVRegistrationEvent <-
                saveApplyForCVRegistrationCommand(
                  input.toUnvalidatedApplyForCVRegistration
                )

              verifiedCVRegistrationEvent <-
                verifyCVRegistrationCommand(
                  savedApplyForCVRegistrationEvent.unvalidatedApplyForCVRegistration
                )

              notifiedCVRegistrationEvent <-
                notifyCVRegistrationResult(
                  verifiedCVRegistrationEvent.verifiedApplyForCVRegistration
                )
            } yield {
              ApplyForCVRegistrationOutput(
                Seq(
                  savedApplyForCVRegistrationEvent,
                  verifiedCVRegistrationEvent,
                  notifiedCVRegistrationEvent
                )
              )
            }

  case class ApplyForCVRegistrationInput(
      givenName: String,
      familyName: String,
      year: Int,
      month: Int,
      day: Int,
      mailAddress: String
  ) {
    def toUnvalidatedApplyForCVRegistration: UnvalidatedApplyForCVRegistration =
      UnvalidatedApplyForCVRegistration(
        UnvalidatedName(givenName, familyName),
        UnvalidatedBirthday(year, month, day),
        UnvalidatedMailAddress(mailAddress)
      )
  }
  case class ApplyForCVRegistrationOutput(events: Seq[Event])
}
