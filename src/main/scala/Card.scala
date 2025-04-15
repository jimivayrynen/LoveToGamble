

case class PlayingCard(val value: String, val suit: String):
  override def toString: String = s"$value of $suit"

  def imagePath: String =
    s"${value.toLowerCase}_of_${suit.toLowerCase}.png"
    
    
    
object PlayingCard:
  def fromString(str: String): PlayingCard =
    val parts = str.split("_of_")
    val value = parts(0).capitalize
    val suit = parts(1).capitalize
    PlayingCard(value, suit)


