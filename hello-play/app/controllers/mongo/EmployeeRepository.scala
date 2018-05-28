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

  def addEmployee(emp: Employee)(implicit ec: ExecutionContext): Future[String] = {
    collection.flatMap(_.insert[Employee](emp)).map { _ =>
      "Employee created successfully \n" + emp
    }
  }

  def employeeWithAge(age: Int)(implicit ec: ExecutionContext): Future[Option[Employee]] =
    collection.flatMap(_.find(Json.obj({
      "age" -> age
    })).one[Employee])

  def employees(implicit ec: ExecutionContext): Future[Seq[Employee]] =
    collection.flatMap(_.find(Json.obj())
      .cursor[Employee]()
      .collect[Seq](100, Cursor.FailOnError[Seq[Employee]]())
    )

  def countEmployees(implicit ec: ExecutionContext): Future[Int] =
    collection.flatMap(_.count())

  def remove(age: Int)(implicit ec: ExecutionContext): Future[Option[Employee]] =
    collection.flatMap(_.findAndRemove(Json.obj({
      "age" -> age
    })).map(_.result[Employee]))

  def increaseAgeByTwo(age: Int)(implicit ec: ExecutionContext): Future[Option[Employee]] = {
    // could be converted to json like other objects
    val selector = BSONDocument("age" -> age)
    val update = BSONDocument(
      "$set" -> BSONDocument("age" -> (age+2))
    )

    collection.flatMap(_.findAndUpdate(selector, update, fetchNewObject = true).map(_.result[Employee]))
  }


}
