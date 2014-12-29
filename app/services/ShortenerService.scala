package services

import scaldi.Injector
import scaldi.Injectable

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

trait Caching extends ShortenerService {
  
  import play.api.Play.current
  import play.api.cache.Cache
  
  abstract override def retrieve(key:String):Option[String] = 
    Cache.getOrElse[Option[String]](key)(super.retrieve(key))
     
  abstract override def shorten(url:String):String = {
    val result = super.shorten(url)
    Cache.set(result, Some(url))
    result
  }
}

class AnormShortnerService(implicit inj:Injector) extends ShortenerService with Injectable {  
  
  val keyGenerator = inject [String => String] (identified by 'keyGenerator)

  import anorm.SQL 
  import play.api.db.DB
  import play.api.Play.current
  
  def retrieve(key:String):Option[String] = DB.withConnection { implicit c =>
    val findOriginal = SQL("select original from shortened where key = {key}").on("key" -> key)
    findOriginal().headOption.map(row => row[String]("original"))
  }
     
  def shorten(url:String):String = {
    val key = keyGenerator(url)
    DB.withConnection { implicit c =>
        SQL("merge into shortened(key, original) key (key) values ({key}, {original})")
          .on("key" -> key, "original" -> url).executeInsert()
    }
    key
  }
}