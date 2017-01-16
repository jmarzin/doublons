import java.sql.{DriverManager, ResultSet}
import scala.collection.immutable.Queue

/**
  * Created by jacquesmarzin on 03/01/2017.
  */
object Base {
  Class.forName("org.sqlite.JDBC")

  private val connection = DriverManager.getConnection("jdbc:sqlite:/Users/jacquesmarzin/ScalaProjects/doublons/restes.db")
  private val statement = connection.createStatement

  def litBase : Vector[Quadruplet] = {
    var queue = Queue[Quadruplet]()
    val res = statement.executeQuery("select * from doublon;")
    while (res.next) {
      queue = queue.enqueue(Quadruplet(res.getString(1),res.getString(2), res.getInt(3),res.getString(4)))
    }
    queue.toVector
  }
  def litBase(code: String): ResultSet = {
    statement.executeQuery("select * from debiteur t1,adresse t2 where listeConsolidation = '"+code+"' and t1.refAdresse = t2.rowid;")
  }
  def miseAJour(quad: Quadruplet, statut: String) : Unit = {
    statement.execute("update doublon set statut = '"+statut+"' where id1 = '"+quad.id1+"' and id2 ='"+quad.id2+"';")
  }
}
