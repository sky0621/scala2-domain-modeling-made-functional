package com.example.cv.implementation

import com.example.cv.design.Command.{NotifyCVRegistrationResult, SaveApplyForCVRegistrationCommand, VerifyCVRegistrationCommand}

object Command {
  def saveApplyForCVRegistrationCommand[F[_]]
      : SaveApplyForCVRegistrationCommand[F] =
    unvalidatedApplyForCVRegistration => ???

  def verifyCVRegistrationCommand[F[_]]: VerifyCVRegistrationCommand[F] =
    unvalidatedApplyForCVRegistration => ???

  def notifyCVRegistrationResult[F[_]]: NotifyCVRegistrationResult[F] =
    verifiedApplyForCVRegistration => ???
}
