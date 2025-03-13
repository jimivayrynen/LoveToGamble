

class Scoring:

  def calculatePoints(player: Player): Int =
    var points = 0

    if (player.collectedCards.size == 4) then points += 1 //mökki

    points += player.collectedCards.count(_.value == "Ace") // ässät
    points += player.collectedCards.count(card => card.value == "10" && card.suit == "Diamonds") * 2 // ruutu-10
    points += player.collectedCards.count(card => card.value == "2" && card.suit == "Spades") // pata-2
    points += player.collectedCards.count(card => card.suit == "Spades") * 2 // eniten patoja saanut pelaaja

    if ( player.collectedCards.size > 10) then points += 1  // yli 10 pistettä

    points

  // päivitetään pelaajan pisteet
  def updateScore(player: Player): Unit =
    player.points =calculatePoints(player)

  // määrittää voittajan
  def checkWinner(players: List[Player]): Player =
    val winner = players.maxBy(_.points)
    winner


end Scoring
