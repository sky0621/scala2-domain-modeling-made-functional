package com.example.cv.implementation.domain

import com.example.cv.implementation.domain.Model.Token

import scala.util.Random
import scala.util.chaining._

object TokenService {
  private val AlphaNumericChars =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
  private val TokenLength = 32

  def generateToken: Token = {
    val random = new Random
    (1 to TokenLength)
      .map(_ => AlphaNumericChars(random.nextInt(AlphaNumericChars.length)))
      .mkString
      .pipe(Token)
  }
}
