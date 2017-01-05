import org.scalatest.{FlatSpec, FunSpec, Matchers}

/**
  * Created by jmarzin-cp on 03/01/2017.
  */
class DoublonApp$Test extends FunSpec with Matchers {
  describe("Le traitement aligne") {
    it("aligne 2 chaines") {
      DoublonApp.aligne(List(List("123"),List("1"))) should be(List(List("123"),List("1  ")))
    }
    it("aligne 3 chaines") {
      DoublonApp.aligne(List(List("123"),List("1"),List("1234"))) should be(List(List("123 "),List("1   "),List("1234")))
    }
    it("aligne 2 liste de 2 chaines") {
      DoublonApp.aligne(List(List("123","1"),List("1","123"))).shouldBe(List(List("123","1  "),List("1  ","123"))).toString()
    }
  }
}

