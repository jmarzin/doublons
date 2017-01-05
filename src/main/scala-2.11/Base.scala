import java.sql.DriverManager

import scala.collection.immutable.Queue

/**
  * Created by jacquesmarzin on 03/01/2017.
  */
object Base {

  Class.forName("org.sqlite.JDBC")

  private val connection = DriverManager.getConnection("jdbc:sqlite:C:\\tiers\\restes.db")
  private val statement = connection.createStatement

  def litBase : Vector[Quadruplet] = {
    var queue = Queue[Quadruplet]()
    val res = statement.executeQuery("select * from doublon;")
    while (res.next) {
      queue = queue.enqueue(Quadruplet(res.getString(1),res.getString(2), res.getInt(3),res.getString(4)))
    }
    return queue.toVector
  }
  def litDeb(code: String): List[String] = {
    return List("")
  }
}
