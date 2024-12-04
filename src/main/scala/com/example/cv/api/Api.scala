package com.example.cv.api

trait Api {
  def execute(request: Request): Response
}
