package controllers.connect

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import connectors.ApplicationConnection
import javax.inject.{Inject, Singleton}
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
        Ok("result back from connection  \n\n" + res)
      }
  }

}
