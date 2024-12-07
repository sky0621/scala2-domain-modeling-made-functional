package com.example.cv.implementation.api

import com.example.cv.implementation.domain.DomainError

sealed trait ApiError
object ApiError {
  def toApiError(e: DomainError): ApiError = InternalServerError(e.message)
}

case class BadRequest(cause: String) extends ApiError
case class InternalServerError(cause: String) extends ApiError
