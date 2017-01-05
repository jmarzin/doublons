
/**
  * Created by jmarzin-cp on 03/01/2017.
  */
object DoublonApp extends App with Utiles{

  val vecteur = Base.litBase
  val aTraiter = (for(v <- vecteur.map(_.id1).distinct) yield {
    val resfiltre = vecteur.filter(p => p.id1 == v || p.id2 == v)
    val restrie = resfiltre.sortWith(_.distance < _.distance)
    (v, restrie.map(p => {
      if(p.id1 == v) {
        (p.id2, p.distance, p.statut)
      }else {
        (p.id1, p.distance, p.statut)
      }}))
  }).sortWith(_._2(0)._2 < _._2(0)._2).sortWith(_._1.length < _._1.length)
  var liste = List[List[String]]()
  for(deb <- aTraiter){
    var debiteur = new Debiteur()
    debiteur.lit(deb._1)
    liste = liste :+ debiteur.listeChampsDeb
    for(deb2 <- deb._2) {
      debiteur.lit(deb2._1)
      liste = liste :+ debiteur.listeDonneesDeb
    }
    liste = aligne(liste)
    liste = List()
  }

  println(vecteur.length)

}
