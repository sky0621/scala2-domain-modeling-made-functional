package com.example.cv.domain

import scala.util.matching.Regex

object Message {
  case class ApprovedCVRegistrationMessage(value: String)
  case class RejectedCVRegistrationMessage(value: String)
}

object Name {
  case class UnvalidatedName(givenName: String, familyName: String)
  case class ValidatedName(givenName: String, familyName: String)
  case class InvalidName(givenName: String, familyName: String)
}

object Birthday {
  case class UnvalidatedBirthday(year: Int, month: Int, day: Int)
  case class ValidatedBirthday(year: Int, month: Int, day: Int)
  case class InvalidBirthday(year: Int, month: Int, day: Int)
}

object MailAddress {
  case class UnvalidatedMailAddress(value: String)
  case class ValidatedMailAddress(value: String)
  case class InvalidMailAddress(value: String)

  def is(maybeMailAddress: UnvalidatedMailAddress): Boolean =
    regex.matches(maybeMailAddress.value)

  private val regex: Regex =
    "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".r
}
