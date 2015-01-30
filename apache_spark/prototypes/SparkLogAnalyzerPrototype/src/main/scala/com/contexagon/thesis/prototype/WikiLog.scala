package com.contexagon.thesis.prototype

/**
 * Created by Sascha P. Lorenz on 24/01/15.
 */


// Case classes can be seen as plain and immutable data-holding objects that should exclusively depend on their constructor arguments.
case class WikiLog (id: Long, timeStamp: Double,
  url: String, dash: String) {

}

object WikiLog{

  //Scala implicitly converts the String to a RichString and invokes the r-method to get an instance of Regex
  val PATTERN = """^(\d+) (\d+(\.\d*)) (.+) (.)""".r

  def parseLogLine(log: String): WikiLog = {
    val res = PATTERN.findFirstMatchIn(log)
    if (res.isEmpty) {
      throw new RuntimeException("Cannot parse log line: " + log)
    }
    val m = res.get
    WikiLog(m.group(1).toLong, m.group(2).toDouble, m.group(3), m.group(4))
  }

}
