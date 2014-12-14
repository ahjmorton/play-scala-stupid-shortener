package modules

import scaldi.Module
import controllers.ShortenerController

class WebModule extends Module{
  binding to new ShortenerController
}