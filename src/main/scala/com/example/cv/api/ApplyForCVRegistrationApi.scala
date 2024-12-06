package com.example.cv.api

import cats.data.EitherT
import cats.instances.future._
import com.example.cv.workflow.{ApplyForCVRegistrationInputDto, ApplyForCVRegistrationOutputDto, ApplyForCVRegistrationWorkflow}

import scala.concurrent.{ExecutionContext, Future}

class ApplyForCVRegistrationApi extends Api[Future, ApiError, Response] {
  override def execute(
      request: Request
  )(implicit ec: ExecutionContext): EitherT[Future, ApiError, Response] = {
    for {
      parsedParameter <- parse(request.values)
      inputDto = ApplyForCVRegistrationInputDto(parsedParameter)
      outputDto <- new ApplyForCVRegistrationWorkflow()
        .execute(inputDto)
        .leftMap[ApiError](e => InternalServerError(e.message))
    } yield ApplyForCVRegistrationResponse(outputDto)
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

case class ApplyForCVRegistrationResponse(
    outputDto: ApplyForCVRegistrationOutputDto
) extends Response
