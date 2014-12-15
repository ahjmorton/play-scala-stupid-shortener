package test

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner
import scaldi.Module
import services.ShortenerService

@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends Specification {
  
  "Shortener" should {
    
    "must display the correct home page" in {
      running(TestServer(3333), HTMLUNIT) { browser =>
        browser.goTo("http://localhost:3333/")

        browser.$(".urlInput").isEmpty !== (true)
      }
    }
    
  }
  
}