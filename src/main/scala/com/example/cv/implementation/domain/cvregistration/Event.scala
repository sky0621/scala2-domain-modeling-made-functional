package com.example.cv.implementation.domain.cvregistration

import com.example.cv.implementation.domain.CompoundModel.{UnvalidatedApplyForCVRegistration, ValidatedApplyForCVRegistration}
import com.example.cv.implementation.domain.Event
import com.example.cv.implementation.domain.Model.Token

sealed trait CVRegistrationEvent extends Event

case class SavedApplyForCVRegistrationEvent(
  unvalidatedApplyForCVRegistration: UnvalidatedApplyForCVRegistration
) extends CVRegistrationEvent

case class ValidatedCVRegistrationEvent(
  validatedApplyForCVRegistration: ValidatedApplyForCVRegistration
) extends CVRegistrationEvent

case class NotifiedCVRegistrationEvent(
  validatedApplyForCVRegistration: ValidatedApplyForCVRegistration,
  token: Token
) extends CVRegistrationEvent
