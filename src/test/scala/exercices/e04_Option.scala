package exercices

import models.devoxx.basic.Room
import support.HandsOnSuite

class e04_Option extends HandsOnSuite {

  /**
    * Option
    *
    *  Option[A] en scala est un conteneur de valeur optionelle de type A
    *  Le type Option est une solution élégante pour resoudre le problème qu'un fontion ne retour pas de valauer,
    *  ansi ca permet d'eviter les NullPointerExceptions
    *  Option a deux sous-types
    *     * Some[A] => presente le cas ou la valeur est presente
    *     * None    => presente l'absence de la valeur
    *
    *  les fonctions de Option resemble beaucoup a celle des collections, ainsi un peut trouver :
    *
    *     map : permet d'appliquer une fonction sur la valeur de l'Option, et elle retour une Option
    *     flatMap : appliquer une fonction qui retourn une Option
    *     get : retourne la valeur de l'Option
    *     getOrElse : permet de retourner une valeur si l'Option egale None
    */
  exercice("Déclarer une Option") {
    // pour declarer une Option on peut utiliser le constructeur de la classe Some
    val age : Option[Int] = Some(42)

    //Utilisez get pour retourner la valeur de l'Option
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
    age.orElse(Option(42)) shouldBe Option(42)
  }


  /**
    * Comme pour les collections, Option a une fontion Map qui permet d'appliquer une fontion sur la valeur de option
    * ainsi l'application de la fonction de type A => B il existe deux  cas :
    *          * si Some[A], le resultat sera une Some[B]
    *          * si None le resultat sera un None
    */
  exercice("Appliquer une fonction sur une Option") {
    // cherchez dans la base de donnée la salle avec un id = 2, en utilisant  RoomRepository.getRoomById
    val room2 : Option[Room] = RoomRepository.getRoom("2")

    // En utilisant la fontion map retournez le nom de la salle
    val room2Name = room2.map(room => room.name)

    room2Name.get shouldBe "salle2"

    // récupérez la salle 15, sachant que la salle 15  n'existe pas
    val room15 = RoomRepository.getRoom("15")

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
    val room3 : Option[Room] = RoomRepository.getRoom("3")

    // recuperer la capacité de la room 3 en utilsiant la fonction RoomRepository.getCapacite(room :Room)
    // la fonction def getCapacite(room) return une  Option[Int]
    val capaciteRoom3: Option[Int] = ??? //room3.flatMap( room => RoomRepository.getCapacite(room) )

    capaciteRoom3.get shouldBe 30

    // appliquer la meme fonction sur une None , quel sera le resultat de retour ?
    val room5 : Option[Room] = RoomRepository.getRoom("5")
    val capaciteRoom5 = room5.flatMap(room => RoomRepository.getCapacite(room) )

    capaciteRoom5 shouldBe None
  }


  /**
    * Some est une case class, alors il est possible d'appliquer du pattern matching
    */
  exercice("Pattern matching sur une Option") {
    val room10 : Option[Room] = RoomRepository.getRoom("10")

    //en appliquant le pattern matching retourner le nom de la salle, sinon "unknown"
    val roomName = room10 match {
      case Some(room) => room.name
      case None => "unknown"
    }

    roomName shouldBe "unknown"
  }

  
  object RoomRepository{

    def getRoom(id: String): Option[Room] = {
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