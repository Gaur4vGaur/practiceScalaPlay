package controllers.jsonpost

import controllers.jsonpost.models.Employee
import javax.inject._
import play.api.libs.json.{JsValue, Json}
import play.api.mvc._

import scala.concurrent.Future

/**
  * Receive and post json
  * @param cc
  */
@Singleton
class EmployeeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  /**
    * The method to expose sample json
    * @param name
    * @return json for employee
    */
  def employee(name: String) = Action.async {
    implicit request: Request[AnyContent] =>
      Future.successful(Ok(Json.toJson[Employee](Employee(name))))
  }

  /**
    * The method to accept and parse json from request body
    * NOTE we need to disable/set CSRF token
    * @return employee attributes
    */
  def update = Action.async(parse.json) {
    implicit request: Request[JsValue] =>
      val employee = request.body.as[Employee]
      Future.successful(Ok(employee.name + " " + employee.id))
  }

}
