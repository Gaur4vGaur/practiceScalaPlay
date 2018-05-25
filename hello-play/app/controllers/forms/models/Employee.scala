package controllers.forms.models

import play.api.data.Form
import play.api.data.Forms._

case class Employee(name: String, age: Int)

object EmployeeForm {

  def form: Form[Employee] = Form(
    mapping(
      "name" -> nonEmptyText,
      "age" -> number
    )(Employee.apply)(Employee.unapply)
  )
}
