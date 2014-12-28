package modules

import scaldi.Module
import services._

class ShortenerModule extends Module{
  
  bind [ShortenerService] to new AnormShortnerService
  
  bind [String => String] identifiedBy 'keyGenerator to (CRC16.apply _)
}