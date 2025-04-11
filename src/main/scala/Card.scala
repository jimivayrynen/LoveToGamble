

case class PlayingCard(val value: String, val suit: String):
  override def toString: String = s"$value of $suit"

  def imagePath: String =
    s"${value.toLowerCase}_of_${suit.toLowerCase}.png"


