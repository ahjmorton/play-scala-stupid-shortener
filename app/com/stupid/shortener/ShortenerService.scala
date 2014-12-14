package com.stupid.shortener

import scaldi.Injector
import scaldi.Injectable

trait ShortenerService {
     def retrieve(key:String):Option[String]
     
     def shorten(url:String):String
}

class MapShortenerService(implicit inj:Injector) extends ShortenerService with Injectable{
  
   private val map = scala.collection.mutable.Map[String, String]()
 
   val keyGenerator = inject [String => String] (identified by 'keyGenerator)
   
   def retrieve(key:String):Option[String] = map get key
     
   def shorten(url:String):String = {
     val key = keyGenerator(url)
     map(key) += url
     key
   }
}