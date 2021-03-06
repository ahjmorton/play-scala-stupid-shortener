package test

import org.specs2.mutable._
import play.api.test._
import play.api.test.Helpers._
import org.junit.runner.RunWith
import org.specs2.runner.JUnitRunner

@RunWith(classOf[JUnitRunner])
class IntegrationSpec extends Specification {
  
  val ServerPort = 3333
  val ServerUrl = "http://localhost:" + ServerPort + "/"
  
  def testServer =  
    TestServer(
      port=ServerPort, 
      application=FakeApplication(additionalConfiguration = inMemoryDatabase())
    )
  
  
  "Shortener" should {
        
    "display the correct home page" in {
      running(testServer, HTMLUNIT) { browser =>
        browser.goTo(ServerUrl)

        browser.$(".urlInput").isEmpty !== (true)
      }
    }
    
    "generate and use a shortened url" in {
      running(testServer, HTMLUNIT) { browser => 
        browser.goTo(ServerUrl)

        val urlInputs = browser.$(".urlInput")
        
        urlInputs.size === (1)
        
        val formNode = urlInputs.first
        
        val testUrl = ServerUrl
        
        browser.submit(".urlInput", ("url", testUrl)).await()
        browser.$(".shortenResult").size === 1
        
        val shortened = browser.$(".shortenResult").first().getText()
        browser.goTo(shortened)
        browser.url() === testUrl
      }
      
    }
    
  }
  
}