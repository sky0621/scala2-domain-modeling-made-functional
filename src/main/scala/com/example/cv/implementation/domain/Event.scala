package com.example.cv.implementation.domain

import com.example.cv.implementation.domain.CompoundModel.{UnvalidatedApplyForCVRegistration, ValidatedApplyForCVRegistration}
import com.example.cv.implementation.domain.Model.Token

trait Event

case class SavedApplyForCVRegistrationEvent(
    unvalidatedApplyForCVRegistration: UnvalidatedApplyForCVRegistration
) extends Event

case class ValidatedCVRegistrationEvent(
    validatedApplyForCVRegistration: ValidatedApplyForCVRegistration
) extends Event

case class NotifiedCVRegistrationEvent(
    validatedApplyForCVRegistration: ValidatedApplyForCVRegistration,
    token: Token
) extends Event
