package io.razem.influxdbclient

import scala.concurrent.Future

protected[influxdbclient] trait DatabaseManagement { self: Database =>

  def create(): Future[QueryResult] =
    exec("CREATE DATABASE \"" + databaseName + "\"")

  def drop(): Future[QueryResult] =
    exec("DROP DATABASE \"" + databaseName + "\"")

  def exists(): Future[Boolean] =
    showDatabases().map(_.contains(databaseName))

}
