

class Game(val players: List[Player]):

  var currentPlayerIndex = 0
  val scoring = new Scoring()
  val board = new Board()
  val deck = new Deck()
  var tableCards: List[PlayingCard] = List() // Kortit pöydällä


  // aloittaa pelin
  def startGame(): Unit =
    deck.shuffleDeck()
    players.foreach(p =>
      val cards = board.dealCards(4)
      p.sethand(cards))
    board.initTable()

  def playCard(card: PlayingCard): Boolean =
    if currentPlayer.hand.contains(card) &&
      CardValidator.validateMove(card, board.tableCards)
      then
      board.addCardToTable(card)
      currentPlayer.playCard(card)
      scoring.updateScore(currentPlayer)
      true
    else
      false


  def currentPlayer: Player = players(currentPlayerIndex)
  // määrittää pelin lopun
  def isGameOver: Boolean =
    players.exists(_.points >= 16)
    
  def winner: Option[Player] =
    val maxPointsPlayer = players.maxBy(_.points)
    if maxPointsPlayer.points >= 16 then Some(maxPointsPlayer) else None



  // päivittää pelin
  private def updateGameState(player: Player): Unit =
    scoring.updateScore(player)
    board.showTable()

  // seuraavan pelaajan vuoro
  def switchTurn(): Unit =
    currentPlayerIndex = (currentPlayerIndex + 1) % players.size


