

class Game(var players: List[Player]):

  var currentPlayerIndex = 0
  val scoring = new Scoring()
  val board = new Board()
  var tableCards: List[PlayingCard] = List() // Kortit pöydällä
  var lastTaker: Option[Player] = None

  // aloittaa pelin
  def startGame(): Unit =
    board.shuffleDeck()
    players.foreach(p =>
      val cards = board.dealCards(4)
      p.sethand(cards))
    board.initTable()

  def playCard(card: PlayingCard, selected: List[PlayingCard]): Boolean =
    if currentPlayer.hand.contains(card) then
      val takes = CardValidator.findValidTakes(card, board.tableCards)

      currentPlayer.playCard(card)

      if takes.nonEmpty then
        val taken = takes.head
        board.tableCards = board.tableCards.filterNot(taken.contains)
        currentPlayer.collectedCards = (card +: taken) ++ currentPlayer.collectedCards
        lastTaker = Some(currentPlayer)

        if board.tableCards.isEmpty then
          currentPlayer.mokkiCount += 1
      else
        board.addCardToTable(card)

      scoring.updateScore(currentPlayer)
      true
    else false


  def currentPlayer: Player = players(currentPlayerIndex)

  // määrittää pelin lopun
  def isGameOver: Boolean =
    players.exists(_.points >= 16) || (board.deck.cardsLeft == 0 && players.forall(_.hand.isEmpty))

  def winner: Option[Player] =
    if players.exists(_.points >= 16) || (board.deck.cardsLeft == 0) then
      Some(players.maxBy(_.points))
    else None


  def endGame():Unit =
    scoring.finalizeScore(players)


  // päivittää pelin
  private def updateGameState(player: Player): Unit =
    scoring.updateScore(player)
    board.showTable()

  // seuraavan pelaajan vuoro
  def switchTurn(): Unit =
    if players.forall(_.hand.isEmpty) then
      lastTaker.foreach(_.collectedCards ++= board.tableCards)
      board.tableCards = Nil
      endGame()
    else
      currentPlayerIndex = (currentPlayerIndex + 1) % players.size

    // Täydennetään seuraavan pelaajan käsi
    val player = currentPlayer
    val missing = 4 - player.hand.size
    if missing > 0 then
      val newCards = board.dealCards(missing)
      player.hand = player.hand ++ newCards

  def copyFrom(other: Game): Unit =
    this.players = other.players
    this.currentPlayerIndex = other.currentPlayerIndex
    this.board.tableCards = other.board.tableCards
    this.board.deck.setCards(other.board.deck.getCards)

