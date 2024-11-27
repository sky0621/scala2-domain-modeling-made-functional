package com.example.cv

object Model {
  case class UnvalidatedMailAddress(value: String)
  case class ValidatedMailAddress(value: String)
  case class InvalidMailAddress(value: String)
}
