package controllers.forms

import controllers.forms.models.{Employee, EmployeeForm}
import controllers.mongo.EmployeeRepository
import javax.inject.{Inject, Singleton}
import play.api.i18n._
import play.api.mvc._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class EmployeeController @Inject()(cc: ControllerComponents, repository: EmployeeRepository) extends AbstractController(cc) with I18nSupport {

  /**
    * Creates user form
    * @return view with form
    */
  def employeeDetails(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      Future.successful(Ok(views.html.employeeForm(EmployeeForm.form)))
  }

  /**
    * Binds user forms input to model and persist it into mongo
    * @return success response if binding is successful
    */
  def employeeDetailsSubmission(): Action[AnyContent] = Action.async {
    implicit request: Request[AnyContent] =>
      EmployeeForm.form.bindFromRequest.fold(
        formWithErrors => {
          Future.successful(BadRequest(views.html.employeeForm(formWithErrors)))
        },
        employeeData => {
          repository.addEmployee[Employee](employeeData).map { result =>
            Ok("data we have received is \n" + result)
          }
        }
      )
  }
}
