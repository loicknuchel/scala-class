package models.devoxx.full

case class Talk(
                 id: TalkId,
                 trackId: String,
                 track: String,
                 talkType: String,
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
