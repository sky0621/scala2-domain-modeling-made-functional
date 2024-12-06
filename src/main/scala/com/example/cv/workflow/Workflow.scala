package com.example.cv.workflow

import cats.data.EitherT
import com.example.cv.domain.DomainError

trait Workflow[F[_], I <: InputDto, E <: DomainError, O <: OutputDto] {
  def execute(input: I): EitherT[F, E, O]
}

trait InputDto

trait OutputDto
