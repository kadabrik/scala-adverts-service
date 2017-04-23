package repositories

import models.Car
import scala.concurrent.Future

trait CarRepository {

  def fetchAll: Future[Seq[Car]]

  def fetchOne(id: String): Future[Option[Car]]

  def create(car: Car): Future[String]

  def update(id: String, car: Car): Future[String]

  def delete(id: String): Future[String]

}
