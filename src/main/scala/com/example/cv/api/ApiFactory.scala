package com.example.cv.api

object ApiFactory {
  def create(apiName: String): Api = apiName match {
    case "applyForCVRegistration" => new ApplyForCVRegistrationApi()
    case _                        => throw new IllegalArgumentException(apiName)
  }
}
