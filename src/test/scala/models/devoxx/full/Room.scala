package models.devoxx.full

case class Room(
                 id: RoomId,
                 name: String,
                 setup: String,
                 capacity: Int,
                 recorded: Option[String]
               )

case class RoomList(
                     content: String,
                     rooms: List[Room]
                   )