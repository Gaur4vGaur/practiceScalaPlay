package controllers.mongo

import javax.inject.Inject
import play.api.libs.json.{JsObject, Json, OFormat}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.api.{Cursor, ReadPreference}
import reactivemongo.bson.BSONDocument
import reactivemongo.play.json.collection.JSONCollection
import reactivemongo.play.json._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

case class Employee(name: String, age: Int)

object Employee {
  implicit val format: OFormat[Employee] = Json.format[Employee]

  def apply(name: String): Employee = {
    Employee(name, scala.util.Random.nextInt(90))
  }
}

class EmployeeRepository @Inject()(reactiveMongoApi: ReactiveMongoApi) {
  private def collection(implicit ec: ExecutionContext): Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("employee"))

  def addEmployee[T](emp: T)(implicit ec: ExecutionContext, format: OFormat[T]): Future[String] =
    collection.flatMap(_.insert[T](emp)).map { _ =>
      "Employee created successfully \n" + emp
    }

  def employeeWithAge[T](age: Int)(implicit ec: ExecutionContext, format: OFormat[T]): Future[Option[T]] =
    collection.flatMap(_.find(Json.obj({
      "age" -> age
    })).one[T])

  def employees[T](implicit ec: ExecutionContext, format: OFormat[T]): Future[Seq[T]] =
    collection.flatMap(_.find(Json.obj())
      .cursor[T]()
      .collect[Seq](100, Cursor.FailOnError[Seq[T]]())
    )

  def countEmployees(implicit ec: ExecutionContext): Future[Int] =
    collection.flatMap(_.count())

  def remove[T](age: Int)(implicit ec: ExecutionContext, format: OFormat[T]): Future[Option[T]] =
    collection.flatMap(_.findAndRemove(Json.obj({
      "age" -> age
    })).map(_.result[T]))

  def increaseAgeByTwo[T](age: Int)(implicit ec: ExecutionContext, format: OFormat[T]): Future[Option[T]] = {
    // could be converted to json like other objects
    val selector = BSONDocument("age" -> age)
    val update = BSONDocument(
      "$set" -> BSONDocument("age" -> (age+2))
    )

    collection.flatMap(_.findAndUpdate(selector, update, fetchNewObject = true).map(_.result[T]))
  }

}
