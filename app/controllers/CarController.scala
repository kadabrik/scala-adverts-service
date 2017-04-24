package controllers

import javax.inject.Inject
import models.Car
import play.api.libs.json._
import play.api.mvc._
import repositories.CarRepository
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class CarController @Inject() (carRepository: CarRepository) extends Controller {

  def index = Action.async {
    carRepository.fetchAll.map(Json.toJson(_)).map(Ok(_))
  }

  def read(id: String) = Action.async {
    carRepository.fetchOne(id).map({
      case Some(car) => Ok(Json.toJson(car))
      case None => NotFound
    })
  }

  def create = Action.async(BodyParsers.parse.json) { implicit request =>
    val carResult = request.body.validate[Car]
    carResult.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("errors" -> JsError.toJson(errors))))
      },
      car => {
        checkCarExists(car.id)(
          Future.successful(Conflict),
          carRepository.create(car).map(id => Created(Json.obj("result" -> id)))
        )
      }
    )
  }

  def update(id: String) = Action.async(BodyParsers.parse.json) { implicit request =>
    val carResult = request.body.validate[Car]
    carResult.fold(
      errors => {
        Future.successful(BadRequest(Json.obj("errors" -> JsError.toJson(errors))))
      },
      car => {
        checkCarExists(id)(
          carRepository.update(id, car).map(id => Ok(Json.obj("result" -> id))),
          Future.successful(NotFound)
        )
      }
    )
  }

  def delete(id: String) = Action.async {
    checkCarExists(id)(
      carRepository.delete(id).map(id => Ok(Json.obj("deleted" -> id))),
      Future.successful(NotFound)
    )
  }

  private def checkCarExists(id: String)(found: => Future[Result], notFound: => Future[Result]) = {
    carRepository.fetchOne(id).flatMap({
      case Some(_) => found
      case None => notFound
    })
  }

}
