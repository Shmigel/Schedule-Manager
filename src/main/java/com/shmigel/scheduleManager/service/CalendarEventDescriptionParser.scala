package com.shmigel.scheduleManager.service

import java.util
import scala.collection.JavaConverters._

class CalendarEventDescriptionParser {

  def split(text: String): util.Map[String, String] = {
    text match {
      case _: String => text.split("\n").filter(_.contains (":") ).map(extractPair).toMap.asJava
      case null => util.Collections.emptyMap()
    }
  }

  def extractPair(text: String): (String, String) = {
    val res = text.split(":").map(_.trim)
    (res(0), res(1))
  }

}
