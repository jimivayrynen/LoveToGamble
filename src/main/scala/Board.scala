

class Board:

  private var tableCards: List[PlayingCard] = List()

  // lisää kortin pöytään
  def addCardTOTable(card: PlayingCard): Unit =
    tableCards = tableCards :+ card

  // poistaa kortin pöydästä, silloin kun pelaaja ottaa sen
  def removeCardFromTabl(card: PlayingCard): Unit =
    tableCards = tableCards.filterNot(_== card)

  // tarkistaa mitkä kortit voidaan ottaa
  def canTakeCards(playercard: PlayingCard): List[PlayingCard] =
    val matchingCards = tableCards.filter( tableCard =>
      tableCard.value == playercard.value)
    matchingCards

  // näyttää pöydällä olevat kortit
  def showTable(): Unit =
    println("Cards on the table:")
    tableCards.foreach(println)

