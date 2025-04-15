

class Scoring:

  // päivitetään pelaajan pisteet
  def updateScore(player: Player): Unit =
    val aces = player.collectedCards.count(_.value == "Ace") // ässät
    val tenOfHearts = player.collectedCards.count(c => c.value == "10" && c.suit == "Diamonds") * 2 //hertta kymppi
    val mokkiPoints = player.mokkiCount // mökit

    player.points = aces + tenOfHearts + mokkiPoints // yhteensä


  // määrittää voittajan
  def finalizeScore(players: List[Player]): Unit =
    players.foreach(updateScore)

    //Eniten kortteja
    val mostCards = players.maxByOption(_.collectedCards.size)
    // Eniten patoja
    val mostSpades = players.maxByOption(_.collectedCards.count(_.suit == "Spades"))

    mostCards.foreach(_.points += 1)
    mostSpades.foreach(_.points += 1)


end Scoring
