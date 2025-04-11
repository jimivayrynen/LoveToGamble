

class Player(val name: String):

  var hand:           List[PlayingCard] = List()
  var collectedCards: List[PlayingCard] = List()
  var points: Int = 0

  // Lisää pelaajalle kortin käteen
  def addCard(card: PlayingCard): Unit =
    hand = hand :+ card

  
  // poistaa pelaajalta kortin kädestä
  def removeCard(card: PlayingCard): Unit =
    hand = hand.filterNot(_ == card)

  
  // Ottaa kortin pöydästä
  def takeCard(cards: List[PlayingCard]): Unit =
    collectedCards = collectedCards ++ cards
    
    
  // pelaa kortin pöytään
  def playCard(card: PlayingCard): Unit =
    hand = hand.filterNot(_ == card)

  def sethand(cards: List[PlayingCard]): Unit =
    hand = cards

  
  // Näyttää käden
  def showHand(): Unit =
    println(s"$name's hand:")
    hand.foreach(println)

  
  // Pisteiden lakseminen pelin lopussa
  def calculatePoints(): Unit =
    points = collectedCards.count(_.value == "Ace") // KAIKKI PISTEET TÄHÄN MITÄ ON TARJOLLA
    println(s"$name has $points points.")