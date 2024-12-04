package com.example.cv.api

class ApplyForCVRegistrationApi extends Api {
  override def execute(request: Request): Response =
    ApplyForCVRegistrationResponse(true)
}

case class ApplyForCVRegistrationResponse(isSuccess: Boolean) extends Response
