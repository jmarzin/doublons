import java.awt.{Dimension, Point}
import javax.swing.event.TableModelListener
import javax.swing.table.TableModel

import scala.collection.mutable
import scala.swing.event.ButtonClicked
import scala.swing.{BoxPanel, Button, Dialog, Label, MainFrame, Orientation, ScrollPane, SimpleSwingApplication, Table}

/**
  * Created by jmarzin-cp on 03/01/2017.
  */
object DoublonApp extends SimpleSwingApplication {

  var vecteur = Base.litBase
  var vecteurDesNonVues = vecteur.filter(_.statut.isEmpty)
  var doublonsSeulement = false
  if (vecteurDesNonVues.nonEmpty) {
    val rep = Dialog.showOptions(
      title = "Validation des doublons",
      message = "Il y a des propositions de doublons qui n'ont pas été examinées.",
      entries = Seq("Les examiner", "Réviser les choix déjà faits"),
      initial = 0)
    if(rep == Dialog.Result.Yes) {
      vecteur = vecteurDesNonVues
    } else if (rep != Dialog.Result.No) {
      doublonsSeulement = true
    }
  }

  def aTraiter(vecteur : Vector[Quadruplet]): Vector[(String, Vector[(String,Int,String)])] = {
    (for (v <- vecteur.map(_.id1).distinct) yield {
      val resfiltre = vecteur.filter(p => p.id1 == v || p.id2 == v)
      val restrie = resfiltre.sortWith(_.distance < _.distance)
      (v, restrie.map(p => {
        if (p.id1 == v) {
          (p.id2, p.distance, p.statut)
        } else {
          (p.id1, p.distance, p.statut)
        }
      }))
    }).sortWith(_._2(0)._2 < _._2(0)._2)//.sortWith(_._1.length < _._1.length)
  }

  var listeATraiter = aTraiter(vecteur) // chargement et tri des données

  def suivant : Array[Array[Any]] = {
    var model: Array[Array[Any]] = Array()
    if(listeATraiter.nonEmpty) {
      val debEnCours = listeATraiter.head
      listeATraiter = listeATraiter.drop(1)
      val debiteur = new Debiteur()
      debiteur.lit(debEnCours._1)
      model = Array(("" :: true :: debiteur.listeDonneesDeb).toArray)
      for (deb2 <- debEnCours._2) {
        debiteur.lit(deb2._1)
        if (deb2._3 == "A"){
          model = model :+ (deb2._2.toString :: true :: debiteur.listeDonneesDeb).toArray[Any]
        } else {
          model = model :+ (deb2._2.toString :: false :: debiteur.listeDonneesDeb).toArray[Any]
        }
      }
    }
    model
  }

  def top = new MainFrame {

    def dimensionneColonnes(): Unit = {
      for(i <- 2 until table.model.getColumnCount) {
        val nomCol = table.peer.getColumnName(i)
        var max = nomCol.length
        for(j <- 0 until table.model.getRowCount) {
          val valeur = table.model.getValueAt(j,i).toString
          if(valeur.length > max) max = valeur.length
        }
        val taille = (max + 1)*8
        table.peer.getColumn(nomCol).setMaxWidth(taille)
        table.peer.getColumn(nomCol).setMinWidth(taille)
      }
    }

    def traiteSaisie(): Unit = {
      pileEcrans.push(encours)
      var filtres: Vector[Quadruplet] = Vector()
      val colonneCode = table.peer.getColumn("listeConsolidation").getModelIndex
      val colonneStatut = table.peer.getColumn("Doublon").getModelIndex
      val nonTagges = encours.filter(_(colonneStatut) == false).map(_(colonneCode))
      val code1 = encours(0)(colonneCode)
      for (code2 <- nonTagges) {
        val filtre = vecteur.filter(p => (p.id1 == code1 && p.id2 == code2) || (p.id2 == code1 && p.id1 == code2))
        Base.miseAJour(filtre.head,"R")
        filtres = filtres ++ filtre.map(q => new Quadruplet(q.id1,q.id2,q.distance,"R"))
        vecteur = vecteur.diff(filtre)
      }
      val tagges = encours.filter(_(colonneStatut) == true).map(_(colonneCode))
      if(tagges.length > 1) {
        for(i <- 0 to tagges.length-2) {
          for(j <- i + 1 until tagges.length) {
            val filtre = vecteur.filter(p => (p.id1 == tagges(i) && p.id2 == tagges(j)) ||
              (p.id2 == tagges(i) && p.id1 == tagges(j)))
            if(filtre.nonEmpty) {
              Base.miseAJour(filtre.head,"A")
            } else {
              println(tagges(i),tagges(j), " couple non trouvé")
            }
            vecteur = vecteur.diff(filtre)
            filtres = filtres ++ filtre.map(q => new Quadruplet(q.id1,q.id2,q.distance,"A"))
          }
        }
      }
      listeATraiter = aTraiter(vecteur)
      pileDoublons.push(filtres)
    }

    title = "Validation des propositions de doublons"
    location = new Point(0, 0)
    preferredSize = new Dimension(1000, 400)
    var pileEcrans = new mutable.Stack[Array[Array[Any]]]
    var pileDoublons = new mutable.Stack[Vector[Quadruplet]]
    val texte = new Label {
      text = "Sélectionnez le ou les doublons du premier débiteur."
    }
    var encours = suivant
    class monModele(var data: Array[Array[Any]], noms: Array[Any]) extends TableModel {
      override def isCellEditable(row: Int, column: Int): Boolean = column == 1 && row > 0
      override def getRowCount = data.length
      override def getColumnClass(columnIndex: Int) = data(0)(columnIndex).getClass
      override def getColumnCount = data(0).length
      override def getColumnName(columnIndex: Int) = noms(columnIndex).toString
      override def removeTableModelListener(l: TableModelListener) = {}
      override def getValueAt(rowIndex: Int, columnIndex: Int) = data(rowIndex)(columnIndex).asInstanceOf[Object]
      override def setValueAt(aValue: scala.Any, rowIndex: Int, columnIndex: Int) = {
        data(rowIndex)(columnIndex) = aValue
      }
      override def addTableModelListener(l: TableModelListener) = {}
    }
    val table = new Table()
    table.model = new monModele(encours, Array("Distance", "Doublon", "nomRs", "prenom", "cpVille",
      "numeroEtVoie", "complementAdresse", "listeConsolidation"))
    table.autoResizeMode = Table.AutoResizeMode.Off
    table.peer.setRowSelectionAllowed(false)
    val boutonValider = new Button {
      text = "Valider"
    }
    if(encours.isEmpty) {
      boutonValider.visible=false
      texte.text = "Il n'y a plus rien à examiner !"
    } else {
      table.peer.getColumn("Distance").setMaxWidth(("Distance".length + 1) * 8)
      table.peer.getColumn("Distance").setMinWidth(("Distance".length + 1) * 8)
      table.peer.getColumn("Doublon").setMaxWidth(("Doublon".length + 1) * 8)
      table.peer.getColumn("Doublon").setMinWidth(("Doublon".length + 1) * 8)

      dimensionneColonnes()
    }

    val boutonAbandonner = new Button {
      text = "Abandonner"
    }

    val boutonPrecedent = new Button {
      text = "Précédent"
      visible = false
    }

    val bas = new BoxPanel(Orientation.Horizontal) {
      contents ++= List(boutonPrecedent, boutonValider, boutonAbandonner)
    }

    var panneau = new ScrollPane(table)

    val ui = new BoxPanel(Orientation.Vertical) {
      contents += panneau
      contents += texte
      contents += bas
    }

    contents = ui

    listenTo(boutonAbandonner)
    listenTo(boutonValider)
    listenTo(boutonPrecedent)

    reactions += {
      case ButtonClicked(`boutonValider`) =>
        traiteSaisie()
        if(listeATraiter.nonEmpty) encours = suivant else encours=Array()
        table.model = new monModele(encours, Array("Distance", "Doublon", "nomRs", "prenom", "cpVille",
          "numeroEtVoie", "complementAdresse", "listeConsolidation"))
        if (encours.nonEmpty) dimensionneColonnes()
        boutonPrecedent.visible = true
      case ButtonClicked(`boutonPrecedent`) =>
        encours = pileEcrans.pop
        vecteur = vecteur ++ pileDoublons.pop
        if(pileEcrans.isEmpty) {
          boutonPrecedent.visible = false
        }
        table.model = new monModele(encours, Array("Distance", "Doublon", "nomRs", "prenom", "cpVille",
          "numeroEtVoie", "complementAdresse", "listeConsolidation"))
        dimensionneColonnes()
      case ButtonClicked(`boutonAbandonner`) =>
          System.exit(0)
    }
  }
}
