package models.devoxx.basic

import org.joda.time.DateTime

case class Slot(
                 id: SlotId,
                 room: RoomId,
                 talk: TalkId,
                 from: DateTime,
                 to: DateTime
               )
