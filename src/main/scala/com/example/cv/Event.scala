package com.example.cv

import com.example.cv.Model._

trait Event

case class AppliedForCVRegistrationEvent(
    maybeMailAddress: UnvalidatedMailAddress
) extends Event

trait VerifiedCVRegistrationEvent extends Event

case class ApprovedCVRegistrationEvent(
    validatedMailAddress: ValidatedMailAddress
) extends VerifiedCVRegistrationEvent

case class RejectedCVRegistrationEvent(
    invalidMailAddress: InvalidMailAddress
) extends VerifiedCVRegistrationEvent

trait NotifiedCVRegistrationEvent extends Event

case class NotifiedApprovedCVRegistrationEvent(
    validatedMailAddress: ValidatedMailAddress,
    approvedCVRegistrationMessage: ApprovedCVRegistrationMessage
) extends NotifiedCVRegistrationEvent

case class NotifiedRejectedCVRegistrationEvent(
    invalidMailAddress: InvalidMailAddress,
    rejectedCVRegistrationMessage: RejectedCVRegistrationMessage
) extends NotifiedCVRegistrationEvent
