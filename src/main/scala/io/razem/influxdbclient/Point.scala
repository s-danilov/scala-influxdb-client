package io.razem.influxdbclient

case class Point(key: String, timestamp: Long = -1, tags: Seq[Tag] = Nil, fields: Seq[Field] = Nil) {
  def addTag(key: String, value: String): Point = copy(tags = Tag(key, value) +: tags)

  /** Optionally append tag value if `value` is not empty. */
  def addOptTag(key: String, value: Option[String]): Point =
    value.map(addTag(key, _)).getOrElse(this)

  def addField(key: String, value: String): Point = copy(fields = StringField(key, value) +: fields)
  def addField(key: String, value: Double): Point = copy(fields = DoubleField(key, value) +: fields)
  def addField(key: String, value: Long): Point = copy(fields = LongField(key, value) +: fields)
  def addField(key: String, value: Boolean): Point = copy(fields = BooleanField(key, value) +: fields)
  def addField(key: String, value: BigDecimal): Point = copy(fields = BigDecimalField(key, value) +: fields)

  def serialize(): String = {
    val sb = new StringBuilder
    sb.append(escapeKey(key))
    if (tags.nonEmpty) {
      sb.append(",")
      sb.append(tags.map(_.serialize).mkString(","))
    }

    if (fields.nonEmpty) {
      sb.append(" ")
      sb.append(fields.map(_.serialize).mkString(","))
    }

    if (timestamp > 0) {
      sb.append(" ")
      sb.append(timestamp)
    }

    sb.toString()
  }

  private def escapeKey(key: String) = key.replaceAll("([ ,])", "\\\\$1")
}
