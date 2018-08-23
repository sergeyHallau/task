package matching.com

import scala.collection.mutable.ListBuffer

object Repository {

  //CLIENTS

  //Should be another approach in real project, of course :)
  var bank: ListBuffer[Client] = ListBuffer.empty

  def getClients: List[Client] = bank.toList.sortWith(_.id < _.id)

  def findClientById(clientId: String): Option[Client] = bank.find(_.id == clientId)

  def addClient(client: Client): Unit = {
    bank += client
    ()
  }

  def updateClient(client: Client): Unit = {
    bank -= bank.find(_.id == client.id).getOrElse(
      throw new RuntimeException(s"Client with ${client.id} not found")
    )
    addClient(client)
  }

  // ORDERS

  var waitingOrders: ListBuffer[Order] = ListBuffer.empty

  def getWaitingOrders: List[Order] = waitingOrders.toList

  def addWaitingOrder(order: Order): Unit = {
    waitingOrders += order
    ()
  }

  def removeWaitingOrder(order: Order): Unit = {
    waitingOrders -= order
    ()
  }

  def findTrans(order: Order): Option[Order] =
    waitingOrders.find(waitingOrder ⇒
      waitingOrder.clientId != order.clientId &&
        waitingOrder.operation != order.operation &&
        waitingOrder.security == order.security &&
        waitingOrder.price == order.price &&
        waitingOrder.quantity == order.quantity
    )


}
