package io.razem.influxdbclient

case class Point(key: String, timestamp: Long = -1, tags: Seq[Tag] = Nil, fields: Seq[Field] = Nil) {
  import Point._

  def addTag(key: String, value: String): Point = copy(tags = Tag(key, value) +: tags)

  /** Optionally append tag value if `value` is not empty. */
  def addOptTag(key: String, value: Option[String]): Point =
    value.map(addTag(key, _)).getOrElse(this)

  def addField(key: String, value: String): Point = copy(fields = StringField(key, value) +: fields)
  def addField(key: String, value: Double): Point = copy(fields = DoubleField(key, value) +: fields)
  def addField(key: String, value: Long): Point = copy(fields = LongField(key, value) +: fields)
  def addField(key: String, value: Boolean): Point = copy(fields = BooleanField(key, value) +: fields)
  def addField(key: String, value: BigDecimal): Point = copy(fields = BigDecimalField(key, value) +: fields)
  def addField(key: String, value: FieldValue): Point = value match {
    case StringFieldValue(v) => addField(key, v)
    case DoubleFieldValue(v) => addField(key, v)
    case LongFieldValue(v) => addField(key, v)
    case BigDecimalFieldValue(v) => addField(key, v)
    case BooleanFieldValue(v) => addField(key, v)
    case OptionFieldValue(v) => v.fold(this)(addField(key, _))
  }
  def addFields(fields: Map[String, FieldValue]): Point = fields.foldLeft(this)((point, e) => point.addField(e._1, e._2))

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

//noinspection TypeAnnotation
object Point {
  import scala.language.implicitConversions

  sealed trait FieldValue
  case class StringFieldValue(value: String) extends FieldValue
  case class DoubleFieldValue(value: Double) extends FieldValue
  case class LongFieldValue(value: Long) extends FieldValue
  def IntFieldValue(value: Int): FieldValue = LongFieldValue(value)
  case class BigDecimalFieldValue(value: BigDecimal) extends FieldValue
  case class BooleanFieldValue(value: Boolean) extends FieldValue
  case class OptionFieldValue(value: Option[FieldValue]) extends FieldValue

  implicit def toFieldValue[A](value: A)(implicit isFieldValue: IsFieldValue[A]): FieldValue =
    isFieldValue.toFieldValue(value)

  case class IsFieldValue[A](toFieldValue: A => FieldValue)
  implicit val isFieldValueString = IsFieldValue[String](StringFieldValue)
  implicit val isFieldValueDouble = IsFieldValue[Double](DoubleFieldValue)
  implicit val isFieldValueInt = IsFieldValue[Int](IntFieldValue)
  implicit val isFieldValueLong = IsFieldValue[Long](LongFieldValue)
  implicit val isFieldValueBigDecimal = IsFieldValue[BigDecimal](BigDecimalFieldValue)
  implicit val isFieldValueBoolean = IsFieldValue[Boolean](BooleanFieldValue)
  implicit def isFieldValueOption[A](implicit isFieldValue: IsFieldValue[A]) = IsFieldValue[Option[A]](v => OptionFieldValue(v.map(isFieldValue.toFieldValue)))
}
