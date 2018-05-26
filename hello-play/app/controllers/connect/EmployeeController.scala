package controllers.connect

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import connectors.ApplicationConnection
import controllers.jsonpost.models.Employee
import javax.inject.{Inject, Singleton}
import play.api.libs.json.Json
import play.api.libs.ws.ahc.AhcWSClient
import play.api.mvc.{AbstractController, Action, AnyContent, ControllerComponents}

import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class EmployeeController @Inject()(cc: ControllerComponents) extends AbstractController(cc) {

  implicit val system: ActorSystem = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  /**
    * Connects to service to fetch employee data
    * @return response obtained from service
    */
  def exposedEmployee: Action[AnyContent] = Action.async {
    implicit request =>
      val wsClient = AhcWSClient()
      val ac = new ApplicationConnection(ws = wsClient)

      ac.employment
        .andThen { case _ => wsClient.close() }
        .andThen { case _ => system.terminate() }.map { res =>
        val e = Json.parse(res).as[Employee]
        Ok("result back from connection  \n\n" + res + "\n\n json after parsing \n\n\n" + e)
      }
  }

  /**
    * The method to post json to end point
    * @return success if the post request is successful
    */
  def postJson: Action[AnyContent] = Action.async {
    implicit request =>
      val wsClient = AhcWSClient()
      val ac = new ApplicationConnection(ws = wsClient)
      ac.postEmployment(Json.toJson[Employee](Employee("Gaurav")))
        .andThen { case _ => wsClient.close() }
        .andThen { case _ => system.terminate() }.map { res =>
        Ok("json posted and the result is  \n\n" + res)
      }
  }

}
