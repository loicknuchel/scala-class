package models.devoxx.full

case class Schedule(
                     slots: List[Slot]
                   )

case class ScheduleList(
                         links: List[Link]
                       )