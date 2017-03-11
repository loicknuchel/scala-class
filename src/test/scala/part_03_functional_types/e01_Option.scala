package exercices

import support.HandsOnSuite
import org.scalatest.Matchers._
import models.devoxx.basic.Room
import scala.None

class e01_Option extends HandsOnSuite {

  /**
    * Option
    *
    *  Option[A] en scala est un conteneur de valeur optionelle de type T
    *  Option a valeur
    *     * Some[A] => presente le cas ou la valeur est presente
    *     * None    => presente l'absence de la valeur
    *
    */
  exercice("Déclarer une Option") {
    // pour declarer une Option on peut utiliser le constructeur de la classe Some
    val age : Option[Int] = Some(42)

    age.get shouldBe 42

    //S'il ya un doute sur la nulleté de la valeur on peut utiliser le constructeur de Option
    val valeurNull : String = null
    val valeurAbsente : Option[String] = Option(valeurNull)

    valeurAbsente shouldBe None

  }
  /**
    * Dans le cas d'absence de valeur, la fonction getOrElse permet de retourner une valeur dpar defaut
    */

  exercice("Valeur par défaut") {

    val age : Option[Int] = None

    age shouldBe None
    age.getOrElse(42) shouldBe 42
  }

  /**
    * Comme pour les collections, Option a une fontion Map qui permet d'appliquer une fontion sur la valeur de option
    * ainsi l'application de la fonction de type A => B il existe deux  cas :
    *          * si Some[A], le resultat sera une Some[B]
    *          * si None le resultat sera un None
    * */

  exercice("Appliquer une fonction sur une Option") {

    // chercher dans la base de donnée la salle avec un id = 2
    val room2 : Option[Room] = RoomRepository.getRoomById("2")

    // En utilisant la fontion map recuperer le nom de la salle
    val room2Name = room2.map(room => room.name)

    room2Name.get shouldBe "salle2"

    // recuperer la salle 15, sachant que la salle 15  n'existe pas
    val room15 = RoomRepository.getRoomById("15")

    room15 shouldBe None

    // appliquer la meme fonction sur une None , quel sera le resultat de retour ?
    val room15Name = room15.map(room => room.name)

    room15Name shouldBe None
  }

  /**
    * Option possède aussi une fonction flatMap, qui prend une fonction de type A => Option[B]
    * et comme dans le resultat de retour est:
    *          * si Some[A], le resultat sera une Some[B]
    *          * si None le resultat sera un None
    */

  exercice("flatMap sur une Option") {
    // En utilisant la fontion map incrementez l'age
    val room3 : Option[Room] = RoomRepository.getRoomById("3")

    // recuperer la capacité de la room 3 en utilsiant la fonction RoomRepository.getCapacite(room :Room)
    // la fonction def getCapacite(room) return une  Option[Int]
    val capaciteRoom3 = room3.flatMap( room => RoomRepository.getCapacite(room) )

    capaciteRoom3.get shouldBe 30

    // appliquer la meme fonction sur une None , quel sera le resultat de retour ?
    val room5 : Option[Room] = RoomRepository.getRoomById("5")
    val capaciteRoom5 = room5.flatMap(room => RoomRepository.getCapacite(room) )

    capaciteRoom5 shouldBe None
  }

  /**
    * Some est une case class, alors il est possible d'appliquer du pattern matching
    */
  exercice("Pattern matching sur une Option") {
    //en appliquant le pattern matching afficher dans le consolle le nom de la salle
    val room3 : Option[Room] = RoomRepository.getRoomById("3")

    room3 match {
      case Some(room) => println(room.name)
      case None => println("la salle n'exciste pas")
    }

  }

  object RoomRepository{

    def getRoomById(id: String): Option[Room] = {
      if(id.toInt < 8 )
        Some(Room(id,s"salle$id", Some(id.toInt * 10)))
      else
        None
    }

    def getCapacite(room :Room) : Option[Int] = {
      if(room.id.toInt < 4)
        room.capacite
      else
        None
    }
  }
}