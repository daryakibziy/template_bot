import cats.implicits.toFunctorOps
import com.bot4s.telegram.api._
import com.bot4s.telegram.clients.FutureSttpClient
import com.bot4s.telegram.future.{Polling, TelegramBot}
import com.bot4s.telegram.methods.SendMessage
import com.bot4s.telegram.models._
import sttp.client3.SttpBackend
import sttp.client3.okhttp.OkHttpFutureBackend

import scala.concurrent.{Await, Future}
import scala.concurrent.duration.Duration
import com.bot4s.telegram.api.declarative.Command._
import com.bot4s.telegram.api.declarative._

// Это пример бота

object Test extends TelegramBot with App with Commands[Future] {
  implicit val backend: SttpBackend[Future, Any] = OkHttpFutureBackend()   //хз что это такое
  override val client: RequestHandler[Future] = new FutureSttpClient("token")

  override def receiveMessage(msg: Message): Future[Unit] =  // сообщение : ответ
   {
    val text1: Option[String] = msg.text
    text1.fold(Future.successful(())) { text =>
      request(SendMessage(msg.source, text.reverse)).void
    }
  }

  onCommand("/start") { implicit msg =>   // если получает это
    reply("Привет! Это бот-эхо.").void    // то возвращает это
  }
  private val unit1: Unit = Await.result(run(), Duration.Inf)  // жди результат бесконечно
  println(unit1)
}