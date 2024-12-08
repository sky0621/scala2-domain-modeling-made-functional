package com.example.cv.implementation.domain

import com.example.cv.implementation.domain.Birthday.{UnvalidatedBirthday, ValidatedBirthday}
import com.example.cv.implementation.domain.MailAddress.{UnvalidatedMailAddress, ValidatedMailAddress}
import com.example.cv.implementation.domain.Name.{UnvalidatedName, ValidatedName}

object CompoundModel {
  case class UnvalidatedApplyForCVRegistration(
      unvalidatedName: UnvalidatedName,
      unvalidatedBirthday: UnvalidatedBirthday,
      unvalidatedMailAddress: UnvalidatedMailAddress
  )

  case class ValidatedApplyForCVRegistration(
      validatedName: ValidatedName,
      validatedBirthday: ValidatedBirthday,
      validatedMailAddress: ValidatedMailAddress
  )
}
