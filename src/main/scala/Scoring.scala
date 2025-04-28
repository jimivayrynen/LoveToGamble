

class Scoring:

  // päivitetään pelaajan pisteet
  def updateScore(player: Player): Int =
    val aces = player.collectedCards.count(_.value == "Ace") // ässät
    val tenOfHearts = player.collectedCards.count(c => c.value == "10" && c.suit == "Diamonds") * 2 //hertta kymppi
    val mokkiPoints = player.mokkiCount // mökit
    aces + tenOfHearts + mokkiPoints


  // määrittää voittajan
  def finalizeScore(players: List[Player]): Unit =
    players.foreach(player =>
      val roundPoints = updateScore(player)
      player.points += roundPoints
      player.mokkiCount = 0)

    //Eniten kortteja
    val mostCards = players.maxByOption(_.collectedCards.size)
    // Eniten patoja
    val mostSpades = players.maxByOption(_.collectedCards.count(_.suit == "Spades"))

    mostCards.foreach(_.points += 1)
    mostSpades.foreach(_.points += 1)


end Scoring
