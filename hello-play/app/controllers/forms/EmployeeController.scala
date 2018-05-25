package controllers.forms

import controllers.forms.models.EmployeeForm
import javax.inject.{Inject, Singleton}
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

import scala.concurrent.Future

@Singleton
class EmployeeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  def employeeDetails() = Action.async {
    implicit request: Request[AnyContent] =>
      Future.successful(Ok(views.html.index()))
  }
}
