package com.example.cv.implementation.api

import cats.Monad
import cats.data.EitherT
import com.example.cv.implementation.Command.{NotifyCVRegistrationResultCommandImpl, SaveApplyForCVRegistrationCommandImpl, ValidateCVRegistrationCommandImpl}
import com.example.cv.implementation.Workflow.{ApplyForCVRegistrationInput, ApplyForCVRegistrationOutput, applyForCVRegistration}
import com.example.cv.implementation.api.ApiError.toApiError
import com.example.cv.implementation.domain.TokenService.GenerateToken

class ApplyForCVRegistrationApi[F[_]](
                                       saveApplyForCVRegistration: SaveApplyForCVRegistrationCommandImpl[F],
                                       validateCVRegistration: ValidateCVRegistrationCommandImpl[F],
                                       notifyCVRegistrationResult: NotifyCVRegistrationResultCommandImpl[F],
                                       generateToken: GenerateToken
                                     ) extends Api[F] {
  override def execute(
                        request: Request
                      )(implicit monad: Monad[F]): EitherT[F, ApiError, Response] = {
    // @formatter:off
    for {
      input <- ApplyForCVRegistrationApi.parse[F](request.values)(monad)
      output <-
        applyForCVRegistration(monad)(
          saveApplyForCVRegistration,
          validateCVRegistration,
          notifyCVRegistrationResult(generateToken)
        )(input).leftMap(toApiError)
    } yield ApplyForCVRegistrationResponse(output)
    // @formatter:on
  }
}

object ApplyForCVRegistrationApi {
  private def parse[F[_] : Monad](
                                   parameters: Array[String]
                                 ): EitherT[F, ApiError, ApplyForCVRegistrationInput] = {
    EitherT.cond[F](
      parameters.length == 6,
      ApplyForCVRegistrationInput(
        parameters.head,
        parameters(1),
        parameters(2).toInt,
        parameters(3).toInt,
        parameters(4).toInt,
        parameters(5)
      ),
      BadRequest(
        s"invalid parameter: ${parameters.mkString("Array(", ", ", ")")}"
      ): ApiError
    )
  }
}

case class ApplyForCVRegistrationResponse(output: ApplyForCVRegistrationOutput)
  extends Response {
  override def show(): Unit = output.events.foreach(println)
}
