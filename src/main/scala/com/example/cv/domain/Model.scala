package com.example.cv.domain

import com.example.cv.domain.Model.UnvalidatedMailAddress

import scala.util.matching.Regex

object Model {
  case class UnvalidatedMailAddress(value: String)
  case class ValidatedMailAddress(value: String)
  case class InvalidMailAddress(value: String)
  case class ApprovedCVRegistrationMessage(value: String)
  case class RejectedCVRegistrationMessage(value: String)
}

object MailAddress {
  private val regex: Regex =
    "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".r
  def is(maybeMailAddress: UnvalidatedMailAddress): Boolean =
    regex.matches(maybeMailAddress.value)
}
