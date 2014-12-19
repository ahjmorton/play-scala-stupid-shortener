package controllers

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import org.junit.runner.RunWith
import scaldi.Module
import services.ShortenerService
import org.specs2.mock.Mockito
import scaldi.play.ScaldiSupport
import play.api.GlobalSettings
import modules.WebModule
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class ShortenerControllerSpec extends Specification with Mockito {
  
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
    
    "be able to return a shortened url" in {
      running(fakeApp) {
          val fakeShort = "test"
          val url = "test.com"
          shortener.shorten(url) returns fakeShort
          val request = FakeRequest(POST, "/shorten/").withFormUrlEncodedBody("url" -> url)
          val result = route(request).get
        
          status(result) must equalTo(OK)
          contentType(result) must beSome.which(_ == "text/html")
          contentAsString(result) must be containing fakeShort
      }
    }
    
    "redirect a valid shortened url" in {
      running(fakeApp) {
          val fakeShort = "test"
          val url = "test.com"
          shortener.retrieve(fakeShort) returns Some(url)
          val result = route(FakeRequest(GET, "/" + fakeShort)).get
        
          status(result) must equalTo(MOVED_PERMANENTLY)
          redirectLocation(result).get must be containing url
      }
    }
    
    "raise an error with an invalid shortened url" in {
      running(fakeApp) {
          val fakeShort = "test"
          shortener.retrieve(fakeShort) returns None
          val result = route(FakeRequest(GET, "/" + fakeShort)).get
        
          status(result) must equalTo(NOT_FOUND)
      }
    }
  }
}