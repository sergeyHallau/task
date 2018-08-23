package matching.com

import shapeless._
import HList._
import syntax.std.traversable._

import scala.util.Try

case class Client(id: String, usd: Int, a: Int, b: Int, c: Int, d: Int) {

  def updateUsdByOrder(order: Order): Client =
    this.copy(usd = if (order.operation == "s")
      this.usd + order.price * order.quantity
    else
      this.usd - order.price * order.quantity)

  def updateSecurityByOrder(order: Order): Client =
    this.copy(a = if (order.security == "A")
      if (order.operation == "s") this.a - order.quantity else this.a + order.quantity
    else this.a)
      .copy(b = if (order.security == "B")
        if (order.operation == "s") this.b - order.quantity else this.b + order.quantity
      else this.b)
      .copy(c = if (order.security == "C")
        if (order.operation == "s") this.c - order.quantity else this.c + order.quantity
      else this.c)
      .copy(d = if (order.security == "D")
        if (order.operation == "s") this.d - order.quantity else this.d + order.quantity
      else this.d)

  def toLine(): String = this.id + "\t" + this.usd + "\t" + this.a + "\t" + this.b + "\t" + this.c + "\t" + this.d
}

object Client {

  def fromLine(line: String): Client = {
    Try {
      val words: Array[String] = line.split("\\t")

      (Client.apply _).tupled(
        words
          .map(field ⇒ if (words.indexOf(field) != 0) field.toInt else field)
          .toHList[String :: Int :: Int :: Int :: Int :: Int :: HNil].get.tupled)

    }.getOrElse(throw new RuntimeException(s"Unable to parse client line: $line"))

  }
}

