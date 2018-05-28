package controllers.mongo

import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class EmployeeController @Inject() (cc: ControllerComponents, repository: EmployeeRepository) extends AbstractController(cc){

  def createEmployee(name: String): Action[AnyContent] = Action.async{
    implicit request =>
      repository.addEmployee[Employee](Employee(name)).map { result =>
        Ok(result)
      }
  }

  def mongoEmployeeWithAge(age: Int): Action[AnyContent] = Action.async {
    implicit request =>
      repository.employeeWithAge[Employee](age).map {
        case Some(emp) => Ok(emp.toString)
        case None => Ok("No employee with the age")
      }
  }

  def mongoEmployees: Action[AnyContent] = Action.async {
    implicit request =>
      repository.employees[Employee].map(emps => Ok(emps.toString))
  }

  def mongoEmployeeCount: Action[AnyContent] = Action.async {
    implicit request =>
      repository.countEmployees.map(count => Ok(count.toString))
  }

  def removeEmployee(age: Int): Action[AnyContent] = Action.async {
    implicit request =>
      repository.remove[Employee](age).map {
        case Some(emp) => Ok(emp.toString)
        case None => Ok("No employee removed")
      }
  }

  def ageByTwo(age: Int): Action[AnyContent] = Action.async {
    implicit request =>
      repository.increaseAgeByTwo[Employee](age).map {
        case Some(emp) => Ok(emp.toString)
        case None => Ok("No employee removed")
      }
  }

}
