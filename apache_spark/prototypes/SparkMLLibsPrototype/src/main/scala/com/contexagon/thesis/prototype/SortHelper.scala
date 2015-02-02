package com.contexagon.thesis.prototype
import scala.math.Ordering
/**
 * Created by Sascha P. Lorenz on 24/01/15.
 */


object SortHelper {
  object IntegerSort extends Ordering[(String, Int)] {
    def compare(a: (String, Int), b: (String, Int)) = {
      a._2 compare b._2
    }
  }

  object LongSort extends Ordering[(String, Long)] {
    def compare(a: (String, Long), b: (String, Long)) = {
      a._2 compare b._2
    }
  }
}
