package com.example.cv.implementation.domain

import com.example.cv.implementation.domain.Birthday.{InvalidBirthday, UnvalidatedBirthday, ValidBirthday, ValidatedBirthday}
import com.example.cv.implementation.domain.MailAddress.{InvalidMailAddress, UnvalidatedMailAddress, ValidMailAddress, ValidatedMailAddress}
import com.example.cv.implementation.domain.Name.{InvalidName, UnvalidatedName, ValidName, ValidatedName}

object ValidatorService {
  def toValidatedName(name: UnvalidatedName): ValidatedName =
    // @formatter:off
    if (name.givenName.nonEmpty && name.familyName.nonEmpty)
      ValidName(name.givenName, name.familyName)
    else InvalidName(name.givenName, name.familyName)
    // @formatter:on

  def toValidatedBirthday(birthday: UnvalidatedBirthday): ValidatedBirthday =
    // @formatter:off
    if (
      birthday.year >= 1900 &&
      birthday.month >= 1 && birthday.month <= 12 &&
      birthday.day >= 1 && birthday.day <= 31
    ) ValidBirthday(birthday.year, birthday.month, birthday.day)
    else InvalidBirthday(birthday.year, birthday.month, birthday.day)
    // @formatter:on

  def toValidatedMailAddress(
                              mailAddress: UnvalidatedMailAddress
                            ): ValidatedMailAddress =
    // @formatter:off
    if ("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$".r.matches(mailAddress.value))
      ValidMailAddress(mailAddress.value)
    else InvalidMailAddress(mailAddress.value)
    // @formatter:on
}
