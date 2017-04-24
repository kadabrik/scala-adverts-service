package models

import play.api.data.validation.ValidationError
import play.api.libs.json._

import scala.collection.Seq

sealed trait Fuel

case object Gasoline extends Fuel {
  override def toString: String = "gasoline"
}

case object Diesel extends Fuel {
  override def toString: String = "diesel"
}

case object Unspecified extends Fuel {
  override def toString: String = "unspecified"
}

object Fuel {
  def apply(fuelType: String): Fuel = fuelType match {
    case "gasoline" => Gasoline
    case "diesel" => Diesel
    case _ => Unspecified
  }

  implicit val fuelWrites: Writes[Fuel] = new Writes[Fuel] {
    override def writes(o: Fuel): JsValue = JsString(o.toString)
  }

  implicit val fuelReads: Reads[Fuel] = new Reads[Fuel] {
    override def reads(json: JsValue): JsResult[Fuel] = json match {
      case JsString(s) =>
        val fuelType = Fuel(s)

        fuelType match {
          case Unspecified => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.proper_fuel"))))
          case _ => JsSuccess(fuelType)
        }
      case _ => JsError(Seq(JsPath() -> Seq(ValidationError("error.expected.jsstring"))))
    }
  }
}
