package controllers.jsonpost.models

import play.api.libs.json.{JsValue, Json}

case class Employee(name: String, id: Int)

object Employee {

  implicit val format = Json.format[Employee]

  /*def writeEmployee(employee: Employee) = Json.toJson(employee)
  def readEmployee(json: JsValue) = json.as[Employee]*/

  def apply(name: String): Employee = {
    val r = scala.util.Random
    Employee(name, r.nextInt(100))
  }
}
