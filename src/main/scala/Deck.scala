
import scala.util.Random

class Deck():

  private var cards: List[PlayingCard] = createDeck()


  //luo korttipakan
  private def createDeck(): List[PlayingCard] =
    val suits = List("Hearts", "Diamonds", "Clubs", "Spades")
    val values = List("2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace")
    Random.shuffle(for suit <- suits; value <- values yield new PlayingCard(value, suit))



  //sekoittaa korttipakan
  def shuffleDeck(): Unit =
    cards = Random.shuffle(cards)



  //Kortin nostaminen pakasta
  def dealCard(): Option[PlayingCard] =
    cards match
      case Nil => None
      case head :: tail =>
        cards = tail
        Some(head)

  def dealCards(n: Int): List[PlayingCard] =
    (1 to n).flatMap(_ => dealCard()).toList

  //paljonko pakkaa jäljellä
  def cardsLeft: Int = cards.size

  def setCards(newCards: List[PlayingCard]): Unit =
    cards = newCards

  def getCards: List[PlayingCard] = cards


end Deck
