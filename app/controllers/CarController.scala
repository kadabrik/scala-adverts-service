package controllers

import models.Car
import play.api.libs.json._
import play.api.mvc._

// TODO: make actions async
// TODO: implement Futures
class CarController extends Controller {

  def index = Action {
    val json = Json.toJson(Car.list)
    Ok(json)
  }

  def read(id: String) = TODO

  def create: Action[JsValue] = Action(BodyParsers.parse.json) { implicit request =>
    val carResult = request.body.validate[Car]
    carResult.fold(
      errors => {
        BadRequest(Json.obj("errors" -> JsError.toJson(errors)))
      },
      car => {
        val id = Car.create(car)
        Ok(Json.obj("result" -> "saved"))
      }
    )
  }

  def update(id: String) = TODO

  def delete(id: String) = TODO

}
