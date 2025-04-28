import scalafx.application.Platform
import scalafx.scene.paint.Color

import scala.concurrent
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global


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


      // Jos kaikkien kortit pelattu mutta peli ei ole vielä ohi
      if players.forall(_.hand.isEmpty) then
        lastTaker.foreach(_.collectedCards ++= board.tableCards)
        board.tableCards = Nil
        scoring.finalizeScore(players)
        if board.deck.cardsLeft == 0 && (players.forall(_.hand.isEmpty)) then
          startNextRound()

      true
    else false


  def currentPlayer: Player = players(currentPlayerIndex)

  // määrittää pelin lopun
  def isGameOver: Boolean =
    players.exists(_.points >= 16)

  def winner: Option[Player] =
    if players.exists(_.points >= 16) then
      Some(players.maxBy(_.points))
    else None


  def endGame():Unit =
    scoring.finalizeScore(players)

  def startNextRound(): Unit =
    players.foreach(_.hand = List())

    val newDeckCards = players.flatMap(_.collectedCards) ++ board.tableCards
    players.foreach(_.collectedCards = List())
    board.resetDeck(newDeckCards)
    board.tableCards = List()

    players.foreach(p => p.sethand(board.dealCards(4)))
    board.initTable()
    currentPlayerIndex = 0


  // päivittää pelin
  private def updateGameState(player: Player): Unit =
    scoring.updateScore(player)
    board.showTable()

  // seuraavan pelaajan vuoro
  def switchTurn(): Unit =
    if players.forall(_.hand.isEmpty) then
      if board.deck.cardsLeft == 0 then
        endGame()
      else
        startNextRound()
    else
      currentPlayerIndex = (currentPlayerIndex + 1) % players.size

    // Täydennetään seuraavan pelaajan käsi
    val player = currentPlayer
    val missing = 4 - player.hand.size
    if missing > 0 then
      val newCards = board.dealCards(missing)
      player.hand = player.hand ++ newCards

    player match
      case ai: ComputerPlayer =>
        Platform.runLater(
          Main.showTemoraryMessage("Tietokone pelaa...", Color.LightGreen))
          Future {
            Thread.sleep(2000)
            val (card, take) = ai.decideMove(board.tableCards)
            Platform.runLater {
              playCard(card, take)
              switchTurn()
              Main.updateUI()
            }
          }
      case _ =>
        Platform.runLater(Main.updateUI())

  def copyFrom(other: Game): Unit =
    this.players = other.players
    this.currentPlayerIndex = other.currentPlayerIndex
    this.board.tableCards = other.board.tableCards
    this.board.deck.setCards(other.board.deck.getCards)

