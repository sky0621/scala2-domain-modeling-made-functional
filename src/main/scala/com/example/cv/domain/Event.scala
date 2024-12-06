package com.example.cv.domain

import com.example.cv.domain.Birthday.UnvalidatedBirthday
import com.example.cv.domain.MailAddress.{InvalidMailAddress, UnvalidatedMailAddress, ValidatedMailAddress}
import com.example.cv.domain.Message._
import com.example.cv.domain.Name.UnvalidatedName

trait Event

case class AppliedForCVRegistrationEvent(
    maybeName: UnvalidatedName,
    maybeBirthday: UnvalidatedBirthday,
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
