Scala RESTful Web service
=========================

This is an example of simple RESTful service built with Scala and [Play Framework](https://www.playframework.com/). The service allows users to place new car adverts and view, modify and delete existing car adverts.

## Car advert model structure

* **id** (_required_): **string**, UUID;
* **title** (_required_): **string**, e.g. _"Audi A4 Avant"_;
* **fuel** (_required_): gasoline or diesel, could be easily extended later;
* **price** (_required_, _positive_): **integer**;
* **new** (_required_): **boolean**, indicates if car is new or used;
* **mileage** (_only for used cars_, _positive_): **integer**;
* **first registration** (_only for used cars_): **date** without time.

```json
{
	"id": "7d3abfb5-b345-4ec1-a405-3401a86cc641",
	"title": "BMW 540",
	"fuel": "diesel",
	"price": 5000,
	"new": false,
	"mileage": 3500,
	"firstRegistration": "2017-01-01"
}
```

## API

Data exchange is done in JSON format, both input and output

- `GET` `/v1/cars`: fetch the list of all stored cars
- `GET` `/v1/cars/:id`: fetch single car advert entity
- `POST` `/v1/cars` + `<JSON entity as body>`: create new advert
- `PUT` `/v1/cars/:id` + `<JSON entity as body>`: update existing advert
- `DELETE` `/v1/cars/:id`: delete advert by ID

## Running application

This project is a simple Scala Play application and could be run using [SBT](http://www.scala-sbt.org/):

```
→ sbt run
```

Application will be available on `http://localhost:9000` (use API URLs mappings to access desired action)

## Running tests

To run existing tests type:

```
→ sbt test
```
