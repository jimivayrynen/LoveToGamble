

class Board:


  val deck = new Deck()
  var tableCards: List[PlayingCard] = List()

  def shuffleDeck(): Unit =
    deck.shuffleDeck()
  // lisää kortin pöytään
  def addCardToTable(card: PlayingCard): Unit =
    tableCards = tableCards :+ card

  def dealCard(): Option[PlayingCard] = deck.dealCard()

  def dealCards(n: Int): List[PlayingCard] =
    deck.dealCards(n)
    
  def initTable(): Unit =
    tableCards = dealCards(4)

  // tarkistaa mitkä kortit voidaan ottaa
  def canTakeCards(playercard: PlayingCard): List[PlayingCard] =
    tableCards.filter(_.value == playercard.value)

  // nostaa kortin pöydältä
  def drawCard(): Option[PlayingCard] =
    deck.dealCard()

  def remainingCards: Int = deck.cardsLeft


  // näyttää pöydällä olevat kortit
  def showTable(): Unit =
    println("Cards on the table:")
    tableCards.foreach(println)

