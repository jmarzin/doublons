/**
  * Created by jacquesmarzin on 03/01/2017.
  */
class Debiteur {
  var identifiant = ""
  var distance = 0
  var statut = ""
  var nomRs = ""
  var prenom = ""
  var cpVille = ""
  var numeroEtVoie = ""
  var listeConsolidation = ""

  def listeChamps: List[String] = {
    return this.getClass.getDeclaredFields.map(_.getName).toList
  }
}
