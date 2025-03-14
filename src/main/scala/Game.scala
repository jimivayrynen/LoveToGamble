

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

    val winner = scoring.checkWinner(players)
    println(s"Game over! The winner is: ${winner.name}")

  // määrittää pelin lopun
  private def isGameOver: Boolean =
    players.exists(_.points >= 16)

  // jakaa kortit
  private def dealCardsToPlayers(): Unit =
    players.foreach( player =>
      for (_ <- 1 to 4) do
        val card = board.drawCard()
        player.addCard(card))


  // pelaajan vuoro GUI:ssa
  private def playTurn(player: Player): Unit =
    println(s"${player.name}'s turn")
    player.showHand()

    val playerCard = selectCardFromHand(player) //GUI:n jälkeen muutos

    // onko siirto laillinen
    if (CardValidator.validateMove(playerCard, board.tableCards))
      board.addCardToTable(playerCard)
      player.playCard(playerCard)
      scoring.updateScore(player)
      board.showTable()
    else
      println("Invalid move, try again!")

  // Graafinen kortin valinta
  private def selectCardFromHand(player: Player): PlayingCard =
    player.hand.head

  // päivittää pelin
  private def updateGameState(player: Player): Unit =
    scoring.updateScore(player)
    board.showTable()

  // seuraavan pelaajan vuoro
  def switchTurn(): Unit =
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size


