package com.example.cv.domain

object EventHistoryRepository {
  type Save[F[_]] = (String, Int) => F[Int]
}
