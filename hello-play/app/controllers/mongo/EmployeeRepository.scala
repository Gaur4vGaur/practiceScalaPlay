package controllers.mongo

import javax.inject.Inject
import play.api.libs.json.{Json, OFormat}
import play.modules.reactivemongo.ReactiveMongoApi
import reactivemongo.play.json.collection.JSONCollection

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

case class Employee(name: String, age: Int)

object Employee {
  implicit val format: OFormat[Employee] = Json.format[Employee]

  def apply(name: String): Employee = {
    Employee(name, scala.util.Random.nextInt(90))
  }
}

class EmployeeRepository @Inject() (ec: ExecutionContext, reactiveMongoApi: ReactiveMongoApi) {
  def collection(implicit ec: ExecutionContext): Future[JSONCollection] = reactiveMongoApi.database.map(_.collection("employee"))

  def addEmployee(emp: Employee)(implicit ec: ExecutionContext): Future[String] = {
    collection.flatMap(_.insert[Employee](emp)).map { x =>
      "Employee created successfully \n" + emp
    }
  }

}
