import scala.collection.immutable.*

object CardValueHelper:

  // Muuntaa kortin numeerseen arvon
  def cardValue(card: PlayingCard): Int = card.value match
    case "Ace" => 1
    case "Jack" => 11
    case "Queen" => 12
    case "King" => 13
    case n => n.toIntOption.getOrElse(0)

object CardValidator:

  // kaikki mahdolliset kombinaatiot
  def findValidTakes(playerCard: PlayingCard, tableCards: List[PlayingCard]): List[List[PlayingCard]] =
    val target = CardValueHelper.cardValue(playerCard)

    val allCombos = tableCards.toSet.subsets().filter(_.nonEmpty).map(_.toList).toList

    val validCombos = allCombos.filter(c => c.map(CardValueHelper.cardValue).sum == target)

    def nonOverLapping(remaining: List[List[PlayingCard]], acc: List[PlayingCard]): List[List[PlayingCard]] =
      remaining match
        case Nil => List(acc)
        case head :: tail =>
          if head.forall(!acc.contains(_)) then
            val withThis = nonOverLapping(tail, acc ++ head)
            val withoutThis = nonOverLapping(tail, acc)
            withThis ++ withoutThis
          else
            nonOverLapping(tail, acc)

    val merged = nonOverLapping(validCombos,List()).filter(_.nonEmpty)
    val sameValue = tableCards.filter(_.value == playerCard.value)
    val valueMatch = if sameValue.nonEmpty then List(sameValue) else Nil

    merged ++ valueMatch

  // Onko siirto validi eli voiko joko ottaa tai pelata pöytään
  def validateMove(playerCard: PlayingCard, tableCards: List[PlayingCard]): Boolean =
    findValidTakes(playerCard, tableCards).nonEmpty || canPlayCard(playerCard, tableCards)

  // Kortin pelaaminen pöytään on aina sallittua (jos ei oteta mitään)
  def canPlayCard(playerCard: PlayingCard, tableCards: List[PlayingCard]): Boolean = true


