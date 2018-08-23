package matching.com

import shapeless._
import HList._
import syntax.std.traversable._

import scala.util.Try

case class Order(clientId: String, operation: String, security: String, price: Int, quantity: Int)

object Order {

  def fromLine(line: String): Order = {

    Try {
      val words: Array[String] = line.split("\\t")

      (Order.apply _).tupled(
        words
          .map(field ⇒ if (words.indexOf(field) == 3 || words.indexOf(field) == 4) field.toInt else field)
          .toHList[String :: String :: String :: Int :: Int :: HNil].get.tupled)

    }.getOrElse(throw new RuntimeException(s"Unable to parse order line: $line"))

  }
}
