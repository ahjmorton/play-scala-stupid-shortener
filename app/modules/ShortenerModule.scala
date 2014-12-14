package modules

import scaldi.Module
import com.stupid.shortener.ShortenerService
import com.stupid.shortener.MapShortenerService

class ShortenerModule extends Module{
  
  bind [ShortenerService] to new MapShortenerService
  
  bind [String => String] identifiedBy 'keyGenerator to ((s:String) => s)
}