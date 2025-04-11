
import scala.util.Random

class Deck():

  private val suits = List("Hearts", "Diamonds", "Clubs", "Spades")
  private val values = List("2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace")

  private var deck: List[PlayingCard] = createDeck()
  private var cardBuffer = scala.collection.mutable.Buffer(createDeck()*)


  //luo korttipakan
  private def createDeck(): List[PlayingCard] =
    for
      suit <- suits
      value <- values
    yield  new PlayingCard(value, suit)

  //sekoittaa korttipakan
  def shuffleDeck(): Unit =
    deck = Random.shuffle(deck)

  //jaa kortteja pelaajille
  def dealCard(): Option[PlayingCard] =
    if cardBuffer.nonEmpty then Some(cardBuffer.remove(0))
    else None


  //Kortin nostaminen pakasta
  def drawCards(): Option[PlayingCard] =
    deck match
      case Nil => None
      case head :: tail =>
        deck = tail
        Some(head)

  //pakan koko
  def size(): Int = deck.size

  def cardsLeft: Int = cardBuffer.size

end Deck
