

class Game(players: List[Player]):

  private var currentPlayerIndex = 0
  private val scoring = new Scoring()
  private val board = new Board()


  // aloittaa pelin
  def startGame(): Unit =
    dealCardsToPlayers()

    while (!isGameOver) do
      val currentPlayer = players(currentPlayerIndex)
      playTurn(currentPlayer)
      updateGameState(currentPlayer)
      switchTurn()


  private def isGameOver: Boolean =
    players.exists(_.points >= 16)

  private def dealCardsToPlayers(): Unit =
    players.foreach( player =>
      for (_ <- 1 to 4) do
        val card = board.drawCard()
        player.addCard(card))


  // pelaajan vuoro
  private def playTurn(player: Player): Unit =
    println(s"${player.name}'s turn")
    player.showHand()

  // päivittää pelin
  private def updateGameState(player: Player): Unit =
    scoring.updateScore(player)
    board.showTable()

  // seuraavan pelaajan vuoro
  def switchTurn(): Unit =
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size


