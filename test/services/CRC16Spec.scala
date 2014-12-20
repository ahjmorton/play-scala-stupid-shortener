package services

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class CRC16Spec extends Specification {

  "CRC16 implementation" should {
    
    "generate the correct CRC for data" in {
        val input = "www.test.com"
        val expected = "16CD"
        
        CRC16(input) === expected
          
    }
  }
}