package com.example.cv.implementation2.infra

import slick.jdbc.JdbcBackend.Database

object DatabaseAccessor {
  val db = Database.forConfig("sqlite")

}
