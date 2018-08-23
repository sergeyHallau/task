package matching.com

object Exchange {

  def expose(order: Order) = {
    val orderForTrade = Repository.findTrans(order)

    if (orderForTrade.isDefined) {

      val client1 = Repository.findClientById(order.clientId)
      val client2 = Repository.findClientById(orderForTrade.get.clientId)

      if (client1.isEmpty)
        throw new RuntimeException(s"Data inconsistency: client ${order.clientId} has orders, but not exists")
      if (client2.isEmpty)
        throw new RuntimeException(s"Data inconsistency: client ${orderForTrade.get.clientId} has orders, but not exists")

      Repository.updateClient(client1.get
        .updateUsdByOrder(order)
        .updateSecurityByOrder(order))

      Repository.updateClient(client2.get
        .updateUsdByOrder(orderForTrade.get)
        .updateSecurityByOrder(orderForTrade.get))

      Repository.removeWaitingOrder(orderForTrade.get)


    } else
      Repository.addWaitingOrder(order)
  }


}
