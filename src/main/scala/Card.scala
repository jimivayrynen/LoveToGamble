

class PlayingCard(val value: String, val suit: String):
  override def toString: String = s"$value of $suit"
