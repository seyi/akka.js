/*
 * Copyright (C) 2017-2018 Lightbend Inc. <https://www.lightbend.com>
 */

package akka.stream.typed.scaladsl

import scala.concurrent.Future
import akka.actor.typed.ActorRef
import akka.actor.typed.TypedAkkaSpecWithShutdown
import akka.actor.typed.scaladsl.Behaviors
import akka.stream.scaladsl.Sink
import akka.stream.scaladsl.Source
import akka.actor.testkit.typed.scaladsl.ActorTestKit

object CustomGuardianAndMaterializerSpec {

  sealed trait GuardianProtocol
  case class Init(sender: ActorRef[String]) extends GuardianProtocol
  case class Msg(sender: ActorRef[String], msg: String) extends GuardianProtocol
  case object Complete extends GuardianProtocol
  case object Failed extends GuardianProtocol
}

class CustomGuardianAndMaterializerSpec extends ActorTestKit with TypedAkkaSpecWithShutdown {
  import CustomGuardianAndMaterializerSpec._

  val guardian = Behaviors.receive[GuardianProtocol] {
    (_, msg) ⇒ Behaviors.same
  }

  implicit val mat = ActorMaterializer()

  "ActorMaterializer" should {

    "work with typed ActorSystem with custom guardian" in {
      val it: Future[String] = Source.single("hello").runWith(Sink.head)

      Await.result(it) should ===("hello")
      // Find a better way to cross compile this
      // it.futureValue should ===("hello")
    }

  }

}
