package controllers.mongo

import javax.inject.{Inject, Singleton}
import play.api.mvc.{ControllerComponents, AbstractController}
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class EmployeeController @Inject() (cc: ControllerComponents, repository: EmployeeRepository) extends AbstractController(cc){

  def createEmployee(name: String) = Action.async{
    implicit request =>
      repository.addEmployee(Employee(name)).map { result =>
        Ok(result)
      }
  }

}
