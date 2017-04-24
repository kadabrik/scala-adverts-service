import java.util.UUID
import models.{Car, Gasoline}
import org.scalatest.concurrent.ScalaFutures
import repositories.InMemoryCarRepository
import org.specs2.mutable._
import scala.concurrent.ExecutionContext.Implicits.global

class InMemoryCarRepositorySpec extends Specification with ScalaFutures {

  "CarRepository" should {

    "insert new car and fetch it by ID" in {

      val repository = new InMemoryCarRepository
      val id = UUID.randomUUID().toString
      val car = Car(id, "Opel Corsa", Gasoline, 10000, `new` = false, None, None)

      val resId = repository.create(car)

      whenReady(resId) { fetchedId =>
        fetchedId mustEqual id
      }

      val resCar = repository.fetchOne(id)

      whenReady(resCar) { fetchedCar =>
        fetchedCar must beSome(car)
      }
    }

    "fetch all predefined cars" in {
      val repository = new InMemoryCarRepository

      val resFetched = repository.fetchAll

      whenReady(resFetched) { fetchedCars =>
        fetchedCars mustEqual Car.predefinedCars
      }
    }

    "return nothing when trying to fetch non-existing car" in {
      val repository = new InMemoryCarRepository

      val resCar = repository.fetchOne("non-existing-id")

      whenReady(resCar) { fetchedCar =>
        fetchedCar must beNone
      }
    }

    "succesfully update the car and return its ID" in {
      val repository = new InMemoryCarRepository
      val id = UUID.randomUUID().toString
      val car = Car(id, "Car to update", Gasoline, 3000, `new` = false, None, None)
      val carModified = car.copy(title = "Car title after update")

      val result = for {
        _ <- repository.create(car)
        updatedId <- repository.update(id, carModified)
        carUpdated <- repository.fetchOne(id)
      } yield (updatedId, carUpdated)

      whenReady(result) { case (fetchedId, fetchedCar) =>
        fetchedId mustEqual id
        fetchedCar must beSome(carModified)
      }
    }

    "delete the car by ID" in {
      val repository = new InMemoryCarRepository
      val id = Car.predefinedCars.head.id

      val result = for {
        _ <- repository.delete(id)
        fetchedCar <- repository.fetchOne(id)
      } yield fetchedCar

      whenReady(result) { fetchedCar =>
        fetchedCar must beNone
      }
    }

  }

}
