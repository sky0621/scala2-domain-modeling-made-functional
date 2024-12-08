package com.example.cv.implementation.domain

object Model {
  case class Token(value: String)
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
  case class UnvalidatedBirthday(year: Int, month: Int, day: Int)

  sealed trait ValidatedBirthday

  case class ValidBirthday(year: Int, month: Int, day: Int)
      extends ValidatedBirthday

  case class InvalidBirthday(year: Int, month: Int, day: Int)
      extends ValidatedBirthday
}

object MailAddress {
  case class UnvalidatedMailAddress(value: String)

  sealed trait ValidatedMailAddress

  case class ValidMailAddress(value: String) extends ValidatedMailAddress

  case class InvalidMailAddress(value: String) extends ValidatedMailAddress
}
