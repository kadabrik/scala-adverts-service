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
  var list: Map[String, Car] = Map(
    "1" -> Car("1", "VW Golf", Gasoline, 1000, `new` = true, None, None),
    "2" -> Car("2", "BMW 540", Diesel, 5000, `new` = false, Some(3500), Some(LocalDate.now()))
  )

  implicit val carWrites: Writes[Car] = (
    (JsPath \ "id").write[String] and
    (JsPath \ "title").write[String] and
    (JsPath \ "fuel").write[Fuel] and
    (JsPath \ "price").write[Int] and
    (JsPath \ "new").write[Boolean] and
    (JsPath \ "mileage").writeNullable[Int] and
    (JsPath \ "firstRegistration").writeNullable[LocalDate]
  )(unlift(Car.unapply))
}

sealed trait Fuel

case object Gasoline extends Fuel {
  override def toString: String = "gasoline"
}

case object Diesel extends Fuel {
  override def toString: String = "diesel"
}

object Fuel {
  def apply(fuelType: String): Fuel = fuelType match {
    case "gasoline" => Gasoline
    case "diesel" => Diesel
  }

  implicit val fuelWrites: Writes[Fuel] = new Writes[Fuel] {
    override def writes(o: Fuel): JsValue = JsString(o.toString)
  }
}
