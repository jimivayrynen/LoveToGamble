

class Board:

  var tableCards: List[PlayingCard] = List()

  // lisää kortin pöytään
  def addCardToTable(card: PlayingCard): Unit =
    tableCards = tableCards :+ card

  // poistaa kortin pöydästä, silloin kun pelaaja ottaa sen
  def removeCardFromTable(card: PlayingCard): Unit =
    tableCards = tableCards.filterNot(_== card)

  // tarkistaa mitkä kortit voidaan ottaa
  def canTakeCards(playercard: PlayingCard): List[PlayingCard] =
    val matchingCards = tableCards.filter( tableCard =>
      tableCard.value == playercard.value)
    matchingCards

  // nostaa kortin pöydältä
  def drawCard(): PlayingCard =
    if (tableCards.nonEmpty) then
      val card = tableCards.head
      removeCardFromTable(card)
      card
    else
      throw new Exception("No cards left on the table")


  // näyttää pöydällä olevat kortit
  def showTable(): Unit =
    println("Cards on the table:")
    tableCards.foreach(println)

