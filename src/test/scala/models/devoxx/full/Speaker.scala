package models.devoxx.full

case class Speaker(
                    uuid: SpeakerId,
                    firstName: String,
                    lastName: String,
                    bio: String,
                    bioAsHtml: String,
                    avatarURL: Option[String],
                    company: Option[String],
                    blog: Option[String],
                    twitter: Option[String],
                    lang: Option[String],
                    acceptedTalks: List[AcceptedTalk]
                  )
