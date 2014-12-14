package test

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import scaldi.Module
import com.stupid.shortener.ShortenerService
import org.specs2.mock.Mockito
import scaldi.play.ScaldiSupport
import play.api.GlobalSettings
import modules.WebModule

@RunWith(classOf[JUnitRunner])
class ShortenerSpec extends Specification with Mockito {
  
  val shortener = mock[ShortenerService]
  
  class TestModule extends Module {
    bind [ShortenerService] to shortener
  }
  
  object TestGlobal extends GlobalSettings with ScaldiSupport {
    def applicationModule = new TestModule :: new WebModule
  }
  
  def fakeApp = FakeApplication(withGlobal=Some(TestGlobal))
  
  "ShortenerController" should {
    
    "send 404 on a bad request" in {
      running(fakeApp) {
        route(FakeRequest(GET, "/boom/")) must beNone        
      }
    }
    
    "render index page" in {
      running(fakeApp) {
        val home = route(FakeRequest(GET, "/")).get
        
        status(home) must equalTo(OK)
        contentType(home) must beSome.which(_ == "text/html")
      }
    }
    
    "getting a shortened url" in {
      running(fakeApp) {
          val fakeShort = "test"
          val url = "http://test.com"
          shortener.shorten(url) returns fakeShort
          val result = route(FakeRequest(GET, "/shorten?url=" + url)).get
        
          status(result) must equalTo(OK)
          contentType(result) must beSome.which(_ == "text/html")
          contentAsString(result) must be containing fakeShort
      }
    }
  }
}