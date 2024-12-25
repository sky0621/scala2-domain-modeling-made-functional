package com.example.cv.implementation.api

import cats.Monad
import cats.data.EitherT
import com.example.cv.design.Service.GenerateToken
import com.example.cv.implementation.Command.NotifyCVRegistrationResultCommandImpl
import com.example.cv.implementation.Workflow.applyForCVRegistration
import com.example.cv.implementation.api.ApiError.toApiError
import com.example.cv.implementation.domain.Birthday.UnvalidatedBirthday
import com.example.cv.implementation.domain.CompoundModel.UnvalidatedApplyForCVRegistration
import com.example.cv.implementation.domain.Event
import com.example.cv.implementation.domain.MailAddress.UnvalidatedMailAddress
import com.example.cv.implementation.domain.Name.UnvalidatedName
import com.example.cv.implementation.domain.cvregistration.CVRegistrationValidator
import com.example.cv.implementation.infra.CVRegistrationInfra

import scala.concurrent.{ExecutionContext, Future}

class ApplyForCVRegistrationApi(
                                 saveUnvalidatedApply: CVRegistrationInfra.SaveUnvalidatedApply[Future],
                                 validate: CVRegistrationValidator.ValidateCVRegistration[Future],
                                 notifyCVRegistrationResult: NotifyCVRegistrationResultCommandImpl[Future],
                                 generateToken: GenerateToken
                               ) extends Api[Future] {

  override def execute(
                        request: Request
                      )(
                        implicit
                        monad: Monad[Future],
                        ec: ExecutionContext
                      ): EitherT[Future, ApiError, Response] = {
    // @formatter:off
    for {
      unvalidatedApplyForCVRegistration <- ApplyForCVRegistrationApi.parse[Future](request.values)(monad)
      events <-
        applyForCVRegistration(monad)(
          saveUnvalidatedApply(ec),
          validate,
          notifyCVRegistrationResult(generateToken)
        )(unvalidatedApplyForCVRegistration).leftMap(toApiError)
    } yield ApplyForCVRegistrationResponse(events)
    // @formatter:on
  }
}

object ApplyForCVRegistrationApi {

  private def parse[F[_] : Monad](
                                   parameters: Array[String]
                                 ): EitherT[F, ApiError, UnvalidatedApplyForCVRegistration] = {
    EitherT.cond[F](
      parameters.length == 6,
      UnvalidatedApplyForCVRegistration(
        unvalidatedName = UnvalidatedName(parameters.head, parameters(1)),
        unvalidatedBirthday = UnvalidatedBirthday(
          parameters(2).toInt,
          parameters(3).toInt,
          parameters(4).toInt
        ),
        unvalidatedMailAddress = UnvalidatedMailAddress(parameters(5))
      ),
      BadRequest(
        s"invalid parameter: ${parameters.mkString("Array(", ", ", ")")}"
      ): ApiError
    )
  }
}

case class ApplyForCVRegistrationResponse(events: Seq[Event]) extends Response {
  override def show(): Unit = events.foreach(println)
}
