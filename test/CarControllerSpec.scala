import models.{Car, Gasoline}
import org.scalatestplus.play._
import play.api.libs.json._
import play.api.test._
import play.api.test.Helpers._

class CarControllerSpec extends PlaySpec with OneAppPerTest {

  "Routes" should {

    "send 404 on a bad request" in  {
      route(app, FakeRequest(GET, "/nonexisting")).map(status) mustBe Some(NOT_FOUND)
    }

  }

  "CarController" should {

    "fetch all stored cars" in new TestContext {
      val result = route(app, FakeRequest(GET, controllerPath)).get
      status(result) mustBe OK
      contentAsJson(result) mustBe JsArray(Car.predefinedCars.map(carToJsObject))
    }

    "fetch car by ID" in new TestContext {
      val existingCar = Car.predefinedCars.head
      val result = route(app, FakeRequest(GET, controllerPath + "/" + existingCar.id)).get
      status(result) mustBe OK
      contentAsJson(result) mustBe carToJsObject(existingCar)
    }

    "return NOT_FOUND status if requested car was not found by ID" in new TestContext {
      val result = route(app, FakeRequest(GET, controllerPath + "/non-existing-id")).get
      status(result) mustBe NOT_FOUND
    }

    "create Car via POST request" in new TestContext {
      val car = Car("created-car-id", "Renault Megane", Gasoline, 1000, `new` = true, None, None)
      val result = route(app, FakeRequest(POST, controllerPath).withJsonBody(carToJsObject(car))).get

      status(result) mustBe CREATED
      contentAsJson(result) mustBe Json.obj("result" -> car.id)
    }

    "return BAD_REQUEST if while trying to create car invalid Car model was used" in new TestContext {
      // mileage field is not allowed for new cars
      val car = Car("created-car-id", "Renault Megane", Gasoline, 1000, `new` = true, Some(2000), None)
      val result = route(app, FakeRequest(POST, controllerPath).withJsonBody(carToJsObject(car))).get

      status(result) mustBe BAD_REQUEST
    }

    "update Car by ID via PUT request and successfully fetch it by ID afterwards" in new TestContext {
      val existingCar = Car.predefinedCars.head
      val updatedCar = existingCar.copy(title = "Seat Leon")

      val resultUpdate = route(app, FakeRequest(PUT, controllerPath + "/" + existingCar.id).withJsonBody(
        carToJsObject(updatedCar))).get

      status(resultUpdate) mustBe OK
      contentAsJson(resultUpdate) mustBe Json.obj("result" -> updatedCar.id)

      val resultFetch = route(app, FakeRequest(GET, controllerPath + "/" + existingCar.id)).get

      status(resultFetch) mustBe OK
      contentAsJson(resultFetch) mustBe carToJsObject(updatedCar)
    }

    "return BAD_REQUEST if while trying to update car by ID invalid Car model was used" in new TestContext {
      val existingCar = Car.predefinedCars.head
      // mileage and firstRegistration fields should be provided for the old car
      val invalidCar = existingCar.copy(`new` = false)

      val result = route(app, FakeRequest(PUT, controllerPath + "/" + existingCar.id).withJsonBody(
        carToJsObject(invalidCar))).get

      status(result) mustBe BAD_REQUEST
    }

    "delete Car by ID via DELETE request" in new TestContext {
      val result = route(app, FakeRequest(DELETE, controllerPath + "/non-existing-id")).get
      status(result) mustBe OK
    }

  }

  trait TestContext {
    val controllerPath = "/v1/cars"

    def carToJsObject(car: Car): JsObject = {
      val mileage = car.mileage match {
        case Some(x) => JsNumber(x)
        case _ => JsNull
      }

      val firstRegistration = car.firstRegistration match {
        case Some(x) => JsString(x.toString)
        case _ => JsNull
      }

      JsObject(Seq(
        "id" -> JsString(car.id),
        "title" -> JsString(car.title),
        "fuel" -> JsString(car.fuel.toString),
        "price" -> JsNumber(car.price),
        "new" -> JsBoolean(car.`new`),
        "mileage" -> mileage,
        "firstRegistration" -> firstRegistration
      ).filterNot(elem => elem._2 == JsNull))
    }
  }

}
