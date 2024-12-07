package com.example.cv.implementation2.domain

import cats.data.EitherT

object EventHistoryRepository {
  type Save[F[_]] = (String, Long) => EitherT[F, DomainError, Unit]
}
