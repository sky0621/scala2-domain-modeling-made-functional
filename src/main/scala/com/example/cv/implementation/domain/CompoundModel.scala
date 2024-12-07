package com.example.cv.implementation.domain

import com.example.cv.implementation.domain.Birthday.{UnvalidatedBirthday, VerifiedBirthday}
import com.example.cv.implementation.domain.MailAddress.{UnvalidatedMailAddress, VerifiedMailAddress}
import com.example.cv.implementation.domain.Name.{UnvalidatedName, VerifiedName}

object CompoundModel {
  case class UnvalidatedApplyForCVRegistration(
      unvalidatedName: UnvalidatedName,
      unvalidatedBirthday: UnvalidatedBirthday,
      unvalidatedMailAddress: UnvalidatedMailAddress
  )

  case class VerifiedApplyForCVRegistration(
      verifiedName: VerifiedName,
      verifiedBirthday: VerifiedBirthday,
      verifiedMailAddress: VerifiedMailAddress
  )
}
