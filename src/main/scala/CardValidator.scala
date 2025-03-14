

object CardValidator:

  // Voiko pelaajan kortit yhdistä pöydällä oleviin kortteihin
  def canTakeCards(playerCard: PlayingCard, tableCards: List[PlayingCard]): Boolean =
    val matchingCards = tableCards.filter(_.value == playerCard.value)

    matchingCards.nonEmpty

  // voiko pelaaja laittaa kortin pöytään
  def canPlayCard(playerCard: PlayingCard, tableCards: List[PlayingCard]): Boolean =
    tableCards.isEmpty

  // onko laillinen siirto
  def validateMove(playerCard: PlayingCard, tableCards: List[PlayingCard]): Boolean =
    (canTakeCards(playerCard, tableCards), canPlayCard(playerCard, tableCards)) match
      case (true, _) =>
        println(s"Player can take cards with ${playerCard.value} from the table.")
        true
      case (_, true) =>
        println(s"Player can play card ${playerCard.value} on the table.")
        true
      case _ =>
        println(s"Invalid move with card ${playerCard.value}. Try again.")
        false

