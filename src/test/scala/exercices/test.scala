package exercices

import models.devoxx.full.DevoxxApi
import support.HandsOnSuite

class test extends HandsOnSuite {

  exercice("test") {
    whenReady(DevoxxApi.fillCache()) { _ =>
      println("done")
    }
  }

}
