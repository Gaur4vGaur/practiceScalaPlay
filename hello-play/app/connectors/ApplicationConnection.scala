package connectors

import javax.inject.Inject
import play.api.libs.ws._

import scala.concurrent.{ExecutionContext, Future}

class ApplicationConnection @Inject()(ws: WSClient) {

  def employment(implicit ec: ExecutionContext): Future[String] = {
    val url = "http://localhost:9000/employee/gaurav"
    ws.url(url).get().map(_.body)
  }

}
