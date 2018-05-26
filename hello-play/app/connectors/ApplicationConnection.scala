package connectors

import javax.inject.Inject
import play.api.libs.json.JsValue
import play.api.libs.ws._

import scala.concurrent.{ExecutionContext, Future}

class ApplicationConnection @Inject()(ws: WSClient) {

  def employment(implicit ec: ExecutionContext): Future[String] = {
    val url = "http://localhost:9000/employee/gaurav"
    val request: WSRequest = ws.url(url)
    request.get().map(_.body)
  }

  def postEmployment(json: JsValue)(implicit ec: ExecutionContext): Future[String] = {
    val url = "http://localhost:9000/employee/update"
    val request: WSRequest = ws.url(url)
    request.post(json).map {
      response =>
        response.status match {
          case 200 => "Success"
          case _ => "check your logs " + response.status + "\n\n" + response.body
        }
    }
  }

}
