package controllers.forms.models

import play.api.data.Form
import play.api.data.Forms._
import play.api.libs.json.{Json, OFormat}

case class Employee(name: String, age: Int)

object Employee {
  implicit val format: OFormat[Employee] = Json.format[Employee]
}

object EmployeeForm {

  def form: Form[Employee] = Form(
    mapping(
      "name" -> nonEmptyText,
      "age" -> number
    )(Employee.apply)(Employee.unapply)
  )
}
