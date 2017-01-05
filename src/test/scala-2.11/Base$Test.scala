import org.scalatest.{FunSpec, Matchers}

/**
  * Created by jacquesmarzin on 05/01/2017.
  */
class Base$Test extends FunSpec with Matchers {
  describe("La lecture de la base ") {
    it("renvoit") {
      val essai = new Debiteur()
      val listeChamps = essai.listeChampsDeb
      val listeDonnees = essai.listeDonneesDeb
      listeDonnees should be (List("le nom", "le prénom", "la ville", "l'adresse", "le complément", "le code"))
      listeChamps should be (List("nomRs", "prenom", "cpVille", "numeroEtVoie", "complementAdresse", "listeConsolidation"))
    }
  }
}
