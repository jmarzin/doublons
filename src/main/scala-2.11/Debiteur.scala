/**
  * Created by jacquesmarzin on 03/01/2017.
  */
class Debiteur(var nomRs: String,
    var prenom : String,
    var cpVille : String,
    var numeroEtVoie : String,
    var complementAdresse : String,
    var compteParDefaut : String,
    var listeConsolidation : String) {

  def this() {
    this("", "", "", "", "", "", "")
  }

  def lit(code: String) {
    val rs = Base.litBase(code)
    nomRs = rs.getString("nomRs")
    prenom = rs.getString("prenom")
    cpVille = rs.getString("cpVille")
    numeroEtVoie = rs.getString("numeroEtVoie")
    complementAdresse = rs.getString("complementAdresse")
    compteParDefaut = rs.getString("compteParDefaut")
    listeConsolidation = code
  }

  def listeDonneesDeb: List[String] = {
    List(nomRs,prenom,cpVille,numeroEtVoie,complementAdresse,compteParDefaut,listeConsolidation)
  }
}
