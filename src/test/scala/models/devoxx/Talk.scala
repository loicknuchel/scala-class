package models.devoxx

case class Talk(
                 id: TalkId,
                 talkType: String,
                 trackId: String,
                 track: String,
                 lang: String,
                 title: String,
                 summary: String,
                 summaryAsHtml: String,
                 speakers: List[LinkWithName]
               )

case class AcceptedTalk(
                         id: TalkId,
                         talkType: String,
                         track: String,
                         title: String,
                         links: List[Link]
                       )
