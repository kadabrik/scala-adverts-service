package models

import java.time.LocalDate

import play.api.libs.json._
import play.api.libs.functional.syntax._

case class Car(
  id: String,
  title: String,
  fuel: Fuel,
  price: Int,
  `new`: Boolean,
  mileage: Option[Int],
  firstRegistration: Option[LocalDate]
)

object Car {
//  TODO: implement Repositories
  var list: Map[String, Car] = Map(
    "1" -> Car("1", "VW Golf", Gasoline, 1000, `new` = true, None, None),
    "2" -> Car("2", "BMW 540", Diesel, 5000, `new` = false, Some(3500), Some(LocalDate.now()))
  )

//  TODO: implement Repositories
  def create(c: Car): String = {
    list = list + (c.id -> c)
    c.id
  }

  implicit val carWrites: Writes[Car] = (
    (JsPath \ "id").write[String] and
    (JsPath \ "title").write[String] and
    (JsPath \ "fuel").write[Fuel] and
    (JsPath \ "price").write[Int] and
    (JsPath \ "new").write[Boolean] and
    (JsPath \ "mileage").writeNullable[Int] and
    (JsPath \ "firstRegistration").writeNullable[LocalDate]
  )(unlift(Car.unapply))

  implicit val carReads: Reads[Car] = (
    (JsPath \ "id").read[String] and
      (JsPath \ "title").read[String] and
      (JsPath \ "fuel").read[Fuel] and
      (JsPath \ "price").read[Int] and
      (JsPath \ "new").read[Boolean] and
      (JsPath \ "mileage").readNullable[Int] and
      (JsPath \ "firstRegistration").readNullable[LocalDate]
  )(Car.apply _)
}
