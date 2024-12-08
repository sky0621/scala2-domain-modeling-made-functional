package com.example.cv.implementation.domain

object Model {
  case class Token(value: String)
}

object Message {
  case class ApprovedCVRegistrationMessage(value: String)

  case class RejectedCVRegistrationMessage(value: String)
}

object Name {
  case class UnvalidatedName(givenName: String, familyName: String)

  sealed trait ValidatedName

  case class ValidName(givenName: String, familyName: String)
    extends ValidatedName

  case class InvalidName(givenName: String, familyName: String)
    extends ValidatedName
}

object Birthday {
  case class UnvalidatedBirthday(year: Int, month: Int, day: Int) {
    // @formatter:off
    def isValid: Boolean =
      year > 1900 &&
      month >= 1 && month <= 12 &&
      day >= 1 && day <= 31
    // @formatter:on
  }

  sealed trait ValidatedBirthday

  case class ValidBirthday(year: Int, month: Int, day: Int)
    extends ValidatedBirthday

  case class InvalidBirthday(year: Int, month: Int, day: Int)
    extends ValidatedBirthday
}

object MailAddress {
  case class UnvalidatedMailAddress(value: String) {
    def isValid: Boolean =
      "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".r.matches(value)
  }

  sealed trait ValidatedMailAddress

  case class ValidMailAddress(value: String) extends ValidatedMailAddress

  case class InvalidMailAddress(value: String) extends ValidatedMailAddress
}
