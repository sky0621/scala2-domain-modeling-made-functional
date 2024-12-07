package com.example.cv.implementation2.domain

import com.example.cv.implementation2.domain.Birthday.{UnvalidatedBirthday, ValidatedBirthday, VerifiedBirthday}
import com.example.cv.implementation2.domain.MailAddress.{InvalidMailAddress, UnvalidatedMailAddress, ValidatedMailAddress, VerifiedMailAddress}
import com.example.cv.implementation2.domain.Message._
import com.example.cv.implementation2.domain.Name.{UnvalidatedName, ValidatedName, VerifiedName}

trait Event

case class SavedApplyForCVRegistrationEvent(
    maybeName: UnvalidatedName,
    maybeBirthday: UnvalidatedBirthday,
    maybeMailAddress: UnvalidatedMailAddress
) extends Event

trait VerifiedCVRegistrationEvent extends Event

case class ApprovedCVRegistrationEvent(
    validatedName: ValidatedName,
    validatedBirthday: ValidatedBirthday,
    validatedMailAddress: ValidatedMailAddress
) extends VerifiedCVRegistrationEvent

case class RejectedCVRegistrationEvent(
    verifiedName: VerifiedName,
    verifiedBirthday: VerifiedBirthday,
    verifiedMailAddress: VerifiedMailAddress
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
