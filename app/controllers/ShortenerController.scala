 package controllers

import play.api.mvc.{Controller, Action, Request}

import views.html
import scaldi.Injectable
import services.ShortenerService
import scaldi.Injector

import play.api.data.Form
import play.api.data.Forms._

class ShortenerController(implicit inj: Injector) extends Controller with Injectable {
  
   val shortener = inject [ShortenerService] 
  
   private val urlForm = Form(single("url" -> UrlMapping.url))
   
   def index = Action {
     Ok(html.index(urlForm))
   }
   
   def shorten() = Action { implicit request => 
     urlForm.bindFromRequest.fold(
         formWithErrors => NotFound,
         url => {
           val shortened = shortener.shorten(url)
           val base = (if (request.secure) "https://" else "http://") + request.host
           Ok(html.shorten(base)(url)(shortened))
         }
         
     )
   }
   
   def redirect(key:String) = Action {
     shortener.retrieve(key) match {
       case Some(url) => MovedPermanently(url)
       case _ => NotFound
     }
     
   }
}