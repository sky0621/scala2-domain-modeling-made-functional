package com.example.cv.infra

import slick.jdbc.JdbcBackend.Database

object DatabaseAccessor {
  val db: _root_.slick.jdbc.JdbcBackend.JdbcDatabaseDef =
    Database.forConfig("sqlite")

}
