import java.awt.{Dimension, Point}
import javax.swing.{JScrollPane, JTextArea}

import scala.swing.BorderPanel.Position.{Center, North, South}
import scala.swing.event.ButtonClicked
import scala.swing.{BorderPanel, BoxPanel, Button, MainFrame, Orientation, SimpleSwingApplication}

/**
  * Created by jmarzin-cp on 03/01/2017.
  */
object DoublonApp extends SimpleSwingApplication with Utiles{

  var listeATraiter = aTraiter

  def top = new MainFrame {


    title = "Validation des propositions de doublons"
    location = new Point(0, 0)
    preferredSize = new Dimension(1000, 800)

    val textArea = new JTextArea(1, 200);
    val scroll = new JScrollPane()
    scroll.getViewport.setView(textArea)
    val boutonValider = new Button {
      text = "Valider"
    }
    val boutonAbandonner = new Button {
      text = "Abandonner"
    }

    val bas = new BoxPanel(Orientation.Vertical) {
      contents ++= List(boutonValider, boutonAbandonner)
    }



    listenTo()
    listenTo(boutonAbandonner)
    listenTo(boutonValider)

    reactions += {
      case ButtonClicked(component) =>
        if (component == boutonValider) {
          var liste = List[List[String]]()
          for (deb <- listeATraiter) {
            var debiteur = new Debiteur()
            debiteur.lit(deb._1)
            liste = liste :+ ("distance" :: "statut" :: debiteur.listeChampsDeb)
            liste = liste :+ ("" :: " " :: debiteur.listeDonneesDeb)
            for (deb2 <- deb._2) {
              debiteur.lit(deb2._1)
              liste = liste :+ (deb2._2.toString :: deb2._3 :: debiteur.listeDonneesDeb)
            }
            liste = aligne(liste)
            liste.foreach(println(_))
            System.exit(0)
          }
        }
        if (component == boutonAbandonner) {
          System.exit(0)
        }
      }
    }
}
