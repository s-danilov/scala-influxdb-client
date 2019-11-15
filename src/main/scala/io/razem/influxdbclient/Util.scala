package io.razem.influxdbclient

protected[influxdbclient]
object Util {
  def escapeString(str: String) = str.replaceAll("([ ,=])", "\\\\$1")
}
