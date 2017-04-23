package repositories

import models.Car
import scala.concurrent.Future

class InMemoryCarRepository extends CarRepository {

  private var storage: Map[String, Car] = Car.predefinedCars.map(car => (car.id, car)).toMap

  override def fetchAll: Future[Seq[Car]] = Future.successful {
    storage.map({ case (_, car) => car }).toSeq
  }

  override def fetchOne(id: String): Future[Option[Car]] = Future.successful {
    storage.get(id)
  }

  override def create(car: Car): Future[String] = Future.successful {
    storage = storage + (car.id -> car)
    car.id
  }

  override def update(id: String, car: Car): Future[String] = Future.successful {
    storage = storage + (car.id -> car)
    id
  }

  override def delete(id: String): Future[String] = Future.successful {
    storage = storage - id
    id
  }

}
