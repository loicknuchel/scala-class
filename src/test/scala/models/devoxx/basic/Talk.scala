package models.devoxx.basic

case class Talk(
                 id: TalkId,
                 talkType: TalkType,
                 title: String,
                 summary: String,
                 speakers: List[SpeakerId]
               )

sealed trait TalkType
case object Conference extends TalkType