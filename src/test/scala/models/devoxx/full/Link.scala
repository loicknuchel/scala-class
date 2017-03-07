package models.devoxx.full

case class Link(
                 title: String,
                 href: String,
                 rel: String
               )

case class LinkWithName(
                         name: String,
                         link: Link
                       )
