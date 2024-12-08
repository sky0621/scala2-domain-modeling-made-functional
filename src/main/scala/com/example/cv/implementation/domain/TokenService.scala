package com.example.cv.implementation.domain

import com.example.cv.implementation.domain.Model.Token

import scala.util.Random
import scala.util.chaining._

object TokenService {
  private val AlphaNumericChars =
    "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"

  type GenerateToken = Int => Token

  def generateToken: GenerateToken = tokenLength => {
    val random = new Random
    (1 to tokenLength)
      .map(_ => AlphaNumericChars(random.nextInt(AlphaNumericChars.length)))
      .mkString
      .pipe(Token)
  }
}
