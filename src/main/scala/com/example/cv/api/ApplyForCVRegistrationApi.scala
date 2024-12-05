package com.example.cv.api

import cats.data.EitherT
import cats.instances.future._
import com.example.cv.domain.Model.UnvalidatedMailAddress

import scala.concurrent.{ExecutionContext, Future}

class ApplyForCVRegistrationApi extends Api[Future, ApiError, Response] {
  override def execute(
      request: Request
  )(implicit ec: ExecutionContext): EitherT[Future, ApiError, Response] = {
    for {
      parsedParameter <- parse(request.values)
      dto = ApplyForCVRegistrationDto(parsedParameter)
    } yield ApplyForCVRegistrationResponse(parsedParameter.nonEmpty)
  }

  private def parse(
      parameters: Array[String]
  )(implicit ec: ExecutionContext): EitherT[Future, ApiError, String] = {
    EitherT.cond[Future](
      parameters.length == 1 && parameters.head.nonEmpty,
      parameters.head,
      BadRequest(
        s"invalid parameter: ${parameters.mkString("Array(", ", ", ")")}"
      ): ApiError
    )
  }
}

case class ApplyForCVRegistrationDto(maybeMailAddress: UnvalidatedMailAddress)
    extends AnyVal
object ApplyForCVRegistrationDto {
  def apply(value: String): ApplyForCVRegistrationDto =
    new ApplyForCVRegistrationDto(UnvalidatedMailAddress(value))
}

case class ApplyForCVRegistrationResponse(isSuccess: Boolean) extends Response
