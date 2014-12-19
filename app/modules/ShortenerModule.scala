package modules

import scaldi.Module
import services._

class ShortenerModule extends Module{
  
  bind [ShortenerService] to new MapShortenerService
  
  bind [String => String] identifiedBy 'keyGenerator to ((s:String) => s.hashCode().toString)
}