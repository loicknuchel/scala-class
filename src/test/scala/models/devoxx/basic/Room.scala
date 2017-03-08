package models.devoxx.basic

case class Room(
                 id: RoomId,
                 name: String,
                 capacite : Option[Int]
               )
