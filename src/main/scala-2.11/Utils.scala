/**
  * Created by jacquesmarzin on 04/01/2017.
  */
trait Utils {

    def aligne(ts : List[List[String]]) : List[String] = {
      val longueur = ts(0).length
      if (ts.filter(_.length != longueur).length != 0){
        return List()
      }
      var max = 0
      var listeLongueur = List[Int]()
      for (i <- 0 to ts.length - 1) {
        max = 0
        for (j <- 0 to longueur) {
          if (ts(i)(j).length > max) max = ts(i)(j).length
        }
        listeLongueur = listeLongueur :+ max
        for (j <- 0 to longueur) {
          retour(i)(j) = ts(i)(j) + " " * (max - ts(i)(j).length)
        }
      }
      return List("","","")
    }
}
