/**
  * Created by jacquesmarzin on 04/01/2017.
  */
trait Utiles {

  def aligne(ts : List[List[String]]) : List[List[String]] = {
    val longueur = ts(0).length
    if (ts.filter(_.length != longueur).length != 0){
      return List()
    }
    var max = 0
    val taillesMax = (for (j <- 0 to longueur - 1) yield {
      max = 0
      for (i <- 0 to ts.length - 1) {
        if (ts(i)(j).length > max ) max = ts(i)(j).length
      }
      max
    }).toList
    val retour = (for(liste <- ts) yield {
      (liste zip taillesMax).map(f => f._1 + " " * (f._2 - f._1.length))
    })

    return retour
  }
}
