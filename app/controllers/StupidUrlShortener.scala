package controllers

import play.api.mvc.Controller
import views.html
import play.api.mvc.Action

object StupidUrlShortener extends Controller {
  
   def index = Action {
     Ok(html.index("Hello world"))
   }
}