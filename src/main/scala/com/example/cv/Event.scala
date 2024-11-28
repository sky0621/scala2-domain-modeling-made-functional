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

case class NotifiedApprovedCVRegistrationEvent(
    validatedMailAddress: ValidatedMailAddress,
    approvedCVRegistrationMessage: ApprovedCVRegistrationMessage
) extends Event

case class NotifiedRejectedCVRegistrationEvent(
    invalidMailAddress: InvalidMailAddress,
    rejectedCVRegistrationMessage: RejectedCVRegistrationMessage
) extends Event
