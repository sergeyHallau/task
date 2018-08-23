package matching.com

import java.io._

import scala.io.Source

object Main extends App {

  Source.fromResource("clients.txt").getLines()
    .foreach(line ⇒ Repository.addClient(Client.fromLine(line)))

  val orders = Source.fromResource("orders.txt").getLines()
    .foreach(line ⇒ Exchange.expose(Order.fromLine(line)))

  Repository.getClients.map(_.toLine())


  val writer = new PrintWriter(new File("output.txt"))
  Repository.getClients.foreach(client ⇒ writer.write(client.toLine() + "\n"))
  writer.close()



}
