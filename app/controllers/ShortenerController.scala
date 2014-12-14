package controllers

import play.api.mvc.Controller
import views.html
import play.api.mvc.Action
import scaldi.Injectable
import com.stupid.shortener.ShortenerService
import scaldi.Injector

class ShortenerController(implicit inj: Injector) extends Controller with Injectable {
  
   val shortener = inject [ShortenerService] 
  
   def index = Action {
     Ok(html.index("Hello world"))
   }
   
   def shorten(url:String) = Action {
     val shortened = shortener.shorten(url)
     Ok(html.shorten(url)(shortened))
   }
   
   def redirect(key:String) = Action {
     shortener.retrieve(key) match {
       case Some(url) => Redirect(url)
       case _ => NotFound
     }
     
   }
}