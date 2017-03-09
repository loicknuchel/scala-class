package exercices

import models.devoxx.full.DevoxxApi
import support.HandsOnSuite

class test extends HandsOnSuite {

  exercice("test") {
    val url = "http://cfp.devoxx.fr/api/conferences/DevoxxFR2017/speakers/25b6eeb75c18e3465d5cddf2be297b8863006551"
    whenReady(DevoxxApi.getRooms(useCache = false)) { res =>
      println("res: "+res)
    }
  }

}
