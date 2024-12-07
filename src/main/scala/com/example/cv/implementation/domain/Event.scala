package com.example.cv.implementation.domain

import com.example.cv.implementation.domain.CompoundModel.{UnvalidatedApplyForCVRegistration, VerifiedApplyForCVRegistration}
import com.example.cv.implementation.domain.MailAddress.{InvalidMailAddress, ValidatedMailAddress}
import com.example.cv.implementation.domain.Message.{ApprovedCVRegistrationMessage, RejectedCVRegistrationMessage}

trait Event

case class SavedApplyForCVRegistrationEvent(
    unvalidatedApplyForCVRegistration: UnvalidatedApplyForCVRegistration
) extends Event

case class VerifiedCVRegistrationEvent(
    verifiedApplyForCVRegistration: VerifiedApplyForCVRegistration
) extends Event

trait NotifiedCVRegistrationEvent extends Event

case class NotifiedApprovedCVRegistrationEvent(
    validatedMailAddress: ValidatedMailAddress,
    approvedCVRegistrationMessage: ApprovedCVRegistrationMessage
) extends NotifiedCVRegistrationEvent

case class NotifiedRejectedCVRegistrationEvent(
    invalidMailAddress: InvalidMailAddress,
    rejectedCVRegistrationMessage: RejectedCVRegistrationMessage
) extends NotifiedCVRegistrationEvent
