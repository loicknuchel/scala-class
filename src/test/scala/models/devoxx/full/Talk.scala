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
