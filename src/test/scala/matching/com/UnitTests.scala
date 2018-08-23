package matching.com

import org.scalatest.{FlatSpec, FunSuite, Matchers}

class MatchingTests extends FunSuite {

  val T1 = Client("T1", 10, 10, 10, 10, 10)

  test("Client to line") {
    assert(T1.toLine() == "T1\t10\t10\t10\t10\t10")
  }

  test("Line to Client") {
    assert(T1 == Client.fromLine("T1\t10\t10\t10\t10\t10"))
  }

  test("Unable to parse Client") {
    val line = "T1\t10\t10\t10\t10"
    val thrown = intercept[Exception] {
      Client.fromLine(line)
    }
    assert(thrown.getMessage == s"Unable to parse client line: $line")
  }

  test("Update client usd") {
    val order = Order("T1", "b", "A", 1, 2)
    assert(T1.updateUsdByOrder(order).usd == 8)
  }

  test("Update client security") {
    val order1 = Order("T1", "b", "A", 1, 2)
    val order2 = Order("T1", "s", "C", 1, 2)
    assert(T1.updateSecurityByOrder(order1).a == 12)
    assert(T1.updateSecurityByOrder(order2).c == 8)
  }

  test("Line to Order") {
    assert(Order("T1", "b", "A", 100, 100) == Order.fromLine("T1\tb\tA\t100\t100"))
  }

  test("Exchange trade positive") {
    Repository.addClient(T1)
    Repository.addClient(Client("T2", 10, 10, 10, 10, 10))
    Repository.addWaitingOrder(Order("T1", "b", "A", 1, 3))
    Exchange.expose(Order("T2", "s", "A", 1, 3))
    assert(Repository.findClientById("T1").get.usd == 7)
    assert(Repository.findClientById("T1").get.a == 13)
    assert(Repository.findClientById("T2").get.usd == 13)
    assert(Repository.findClientById("T2").get.a == 7)
    assert(Repository.getWaitingOrders.isEmpty)
  }

  test("Exchange trade negative") {
    Repository.addClient(T1)
    Repository.addClient(Client("T2", 10, 10, 10, 10, 10))
    Repository.addWaitingOrder(Order("T1", "b", "A", 1, 3))
    Exchange.expose(Order("T2", "s", "B", 1, 3))
    Exchange.expose(Order("T2", "b", "A", 1, 3))
    Exchange.expose(Order("T2", "s", "A", 1, 2))
    assert(Repository.getWaitingOrders.size == 4)
  }


}
