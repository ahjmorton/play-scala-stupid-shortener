package services

import scaldi.Injector
import scaldi.Injectable
import anorm.SQL 
import play.api.db.DB

trait ShortenerService {
     def retrieve(key:String):Option[String]
     
     def shorten(url:String):String
}

class MapShortenerService(implicit inj:Injector) extends ShortenerService with Injectable{
  
 
   val keyGenerator = inject [String => String] (identified by 'keyGenerator)
   
   private val map = scala.collection.mutable.Map[String, String]()
   def retrieve(key:String):Option[String] = map get key
     
   def shorten(url:String):String = {
     val key = keyGenerator(url)
     map += (key -> url)
     key
   }
}

class AnormShortnerService(implicit inj:Injector) extends ShortenerService with Injectable {  
  
  val keyGenerator = inject [String => String] (identified by 'keyGenerator)

  import play.api.Play.current
  
  def retrieve(key:String):Option[String] = DB.withConnection { implicit c =>
    val findOriginal = SQL("select original from shortened where key = {key}").on("key" -> key)
    findOriginal().headOption.map(row => row[String]("original"))
  }
     
  def shorten(url:String):String = {
    val key = keyGenerator(url)
    DB.withConnection { implicit c =>
      SQL("insert into shortened(key, original) values ({key}, {original})")
        .on("key" -> key, "original" -> url).executeInsert()
    }
    key
  }
}