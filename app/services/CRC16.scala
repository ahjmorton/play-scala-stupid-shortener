package services

object CRC16 {

  def apply(value:String):String = {
    var crc = 0xFFFF;
    value.getBytes().foreach {byte =>
        crc = ((crc  >>> 8) | (crc  << 8) ) & 0xffff
        crc ^= (byte & 0xff)
        crc ^= ((crc & 0xff) >> 4)
        crc ^= (crc << 12) & 0xffff
        crc ^= ((crc & 0xFF) << 5) & 0xffff
    }
    crc.toHexString.toUpperCase
  }
}