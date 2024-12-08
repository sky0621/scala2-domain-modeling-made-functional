package com.example.cv.implementation.domain

import cats.data.EitherT
import com.example.cv.implementation.domain.CompoundModel.UnvalidatedApplyForCVRegistration

object Repository {
  type SaveUnvalidatedApplyForCVRegistration[F[_]] =
    UnvalidatedApplyForCVRegistration => EitherT[F, DomainError, Unit]
}
