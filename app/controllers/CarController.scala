package controllers

import models.Car
import play.api.libs.json._
import play.api.mvc._

class CarController extends Controller {

  def index = Action {
    val json = Json.toJson(Car.list)
    Ok(json)
  }

  def read(id: String) = TODO

  def create = TODO

  def update(id: String) = TODO

  def delete(id: String) = TODO

}
