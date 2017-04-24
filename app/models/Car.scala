package models

import java.time.LocalDate
import java.util.UUID

import play.api.data.validation.ValidationError
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json._

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
      (JsPath \ "price").read[Int](min(0)) and
      (JsPath \ "new").read[Boolean] and
      (JsPath \ "new").read[Boolean].flatMap({
        case true =>
          (JsPath \ "mileage").readNullable[Int].filter(
            ValidationError("error.unexpected_mileage.must_be_omitted_for_a_new_car")
          )(_.isEmpty)
        case false =>
          (JsPath \ "mileage").readNullable[Int](min(0)).filter(
            ValidationError("error.expected_mileage.must_be_present_for_a_used_car")
          )(_.isDefined)
      }) and
      (JsPath \ "new").read[Boolean].flatMap({
        case true =>
          (JsPath \ "firstRegistration").readNullable[LocalDate].filter(
            ValidationError("error.unexpected_firstRegistration.must_be_omitted_for_a_new_car")
          )(_.isEmpty)
        case false =>
          (JsPath \ "firstRegistration").readNullable[LocalDate].filter(
            ValidationError("error.expected_firstRegistration.must_be_present_for_a_used_car")
          )(_.isDefined)
      })
  )(Car.apply _)

  val predefinedCars = Seq(
    Car(UUID.randomUUID().toString, "VW Golf", Gasoline, 1000, `new` = true, None, None),
    Car(UUID.randomUUID().toString, "BMW 540", Diesel, 5000, `new` = false, Some(3500), Some(LocalDate.now()))
  )
}
