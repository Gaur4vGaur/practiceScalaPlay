package controllers.forms

import controllers.forms.models.EmployeeForm
import javax.inject.{Inject, Singleton}
import play.api.i18n._
import play.api.mvc.{AbstractController, AnyContent, ControllerComponents, Request}

import scala.concurrent.Future

@Singleton
class EmployeeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) with I18nSupport {

  def employeeDetails() = Action.async {
    implicit request: Request[AnyContent] =>
      Future.successful(Ok(views.html.employeeForm(EmployeeForm.form)))
  }

  def employeeDetailsSubmission() = Action.async {
    implicit request: Request[AnyContent] =>
      EmployeeForm.form.bindFromRequest.fold(
        formWithErrors => {
          Future.successful(BadRequest(views.html.employeeForm(formWithErrors)))
        },
        employeeData => {
          Future.successful(Ok("date we have received is " + employeeData.name + " " + employeeData.age))
        }
      )
  }
}
