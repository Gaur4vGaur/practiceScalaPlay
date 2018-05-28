package controllers.mongo

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class EmployeeController @Inject() (cc: ControllerComponents, repository: EmployeeRepository) extends AbstractController(cc){

  def createEmployee(name: String): Action[AnyContent] = Action.async{
    implicit request =>
      repository.addEmployee(Employee(name)).map { result =>
        Ok(result)
      }
  }

  def mongoEmployeeWithAge(age: Int): Action[AnyContent] = Action.async {
    implicit request =>
      repository.employeeWithAge(age).map {
        case Some(emp) => Ok(emp.toString)
        case None => Ok("No employee with the age")
      }
  }

  def mongoEmployees: Action[AnyContent] = Action.async {
    implicit request =>
      repository.employees.map(emps => Ok(emps.toString))
  }

  def mongoEmployeeCount: Action[AnyContent] = Action.async {
    implicit request =>
      repository.countEmployees.map(count => Ok(count.toString))
  }

  def removeEmployee(age: Int): Action[AnyContent] = Action.async {
    implicit request =>
      repository.remove(age).map {
        case Some(emp) => Ok(emp.toString)
        case None => Ok("No employee removed")
      }
  }

  def ageByTwo(age: Int): Action[AnyContent] = Action.async {
    implicit request =>
      repository.increaseAgeByTwo(age).map {
        case Some(emp) => Ok(emp.toString)
        case None => Ok("No employee removed")
      }
  }

}
