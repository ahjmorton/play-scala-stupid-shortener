import scaldi.play.ScaldiSupport
import play.api.GlobalSettings
import modules.{WebModule, ShortenerModule}

object Global extends GlobalSettings with ScaldiSupport {
   def applicationModule = new WebModule :: new ShortenerModule
  
}