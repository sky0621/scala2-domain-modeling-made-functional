package com.example.cv

import com.example.cv.Model.{InvalidMailAddress, UnvalidatedMailAddress, ValidatedMailAddress}

sealed trait Event {
  def name: String
}

case class AppliedForCVRegistrationEvent(
    maybeMailAddress: UnvalidatedMailAddress
) extends Event {
  override def name: String = "appliedForCVRegistration"
}

case object AppliedForCVRegistrationEvent {
  def apply(maybeMailAddress: UnvalidatedMailAddress) =
    new AppliedForCVRegistrationEvent(maybeMailAddress)
}

trait VerifiedCVRegistrationEvent extends Event

case class ApprovedCVRegistrationEvent(
    validatedMailAddress: ValidatedMailAddress
) extends VerifiedCVRegistrationEvent {
  override def name: String = "approvedCVRegistration"
}

case object ApprovedCVRegistrationEvent {
  def apply(validatedMailAddress: ValidatedMailAddress) =
    new ApprovedCVRegistrationEvent(validatedMailAddress)
}

case class RejectedCVRegistrationEvent(
    invalidMailAddress: InvalidMailAddress
) extends VerifiedCVRegistrationEvent {
  override def name: String = "rejectedCVRegistration"
}

case object RejectedCVRegistrationEvent {
  def apply(invalidMailAddress: InvalidMailAddress) =
    new RejectedCVRegistrationEvent(invalidMailAddress)
}
