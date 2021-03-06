package services

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import org.junit.runner.RunWith
import org.specs2.mock.Mockito
import org.specs2.runner.JUnitRunner
import scaldi.Module
import modules.ShortenerModule
import org.specs2.specification.Scope

trait Context extends Scope with Mockito {
  val keyGenerator = mock[String => String]

  class TestModule extends Module {
    bind [String => String] identifiedBy 'keyGenerator to keyGenerator
  }
  
  implicit val applicationModule = new TestModule
    
  lazy val subject = new MapShortenerService
}

@RunWith(classOf[JUnitRunner])
class MapShortenerServiceSpec extends Specification with Context {
  
  "Map based shortener storage" should {
    "return the key generated by the key generator" in {
      val testKey = "test"
      val input = "www.example.com"
      keyGenerator(input) returns testKey
      
      val result = subject.shorten(input)
      
      result === testKey
    }
    
    "return the stored value on a retrieve" in {
      val testKey = "test"
      val input = "www.example.com"
      keyGenerator(input) returns testKey
      
      val key = subject.shorten(input)
      
      key === testKey
      
      val result = subject.retrieve(key)
      
      result must beSome(input)
    }
    
    "return None on a retrieve with no previously stored key" in {

      val result = subject.retrieve("")
      
      result === None
    }
  }
}