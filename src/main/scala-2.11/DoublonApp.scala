import scala.swing.SimpleSwingApplication

/**
  * Created by jmarzin-cp on 03/01/2017.
  */
object DoublonApp extends SimpleSwingApplication with Utiles{

  var listeATraiter = aTraiter



  var liste = List[List[String]]()
  for(deb <- listeATraiter){
    var debiteur = new Debiteur()
    debiteur.lit(deb._1)
    liste = liste :+ ("distance" :: "statut" :: debiteur.listeChampsDeb)
    liste = liste :+ ("" :: " " :: debiteur.listeDonneesDeb)
    for(deb2 <- deb._2) {
      debiteur.lit(deb2._1)
      liste = liste :+ (deb2._2.toString :: deb2._3 :: debiteur.listeDonneesDeb)
    }
    liste = aligne(liste)
    liste.foreach(println(_))
    System.exit(0)
  }
}
