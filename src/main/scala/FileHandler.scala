import java.nio.file.{Files, Paths}
import java.nio.charset.StandardCharsets
import scala.jdk.CollectionConverters._


object FileHandler:

  //tallentaa pelin omaan tekstimuotoon
  def saveGame(game: Game, path: String): Unit =
    val lines = game.players.map( player =>
      val hand = player.hand.map(_.imagePath.stripSuffix(".png")).mkString(",")
      s"PLAYER:${player.name}:${player.points}:$hand"
    ) ++
      Seq(
        "TABLE:" + game.board.tableCards.map(_.imagePath.stripSuffix(".png")).mkString(","),
        "DECK:" + game.board.deck.getCards.map(_.imagePath.stripSuffix(".png")).mkString(","),
        s"TURN:${game.currentPlayer.name}"
      )

       Files.write(Paths.get(path), lines.mkString("\n").getBytes(StandardCharsets.UTF_8))


  def loadGame(path: String): Game =
    val lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8).asScala.toList

    var players: List[Player] = List()
    var tableCards: List[PlayingCard] = List()
    var deckCards: List[PlayingCard] = List()
    var turnPlayerName = ""

    lines.foreach{
      case line if line.startsWith("PLAYER:") =>
        val parts = line.stripPrefix("PLAYER:").split(":")
        val name = parts(0)
        val points = parts(1).toInt
        val hand = if parts.length > 2 && parts(2).nonEmpty then
          parts(2).split(",").toList.map(PlayingCard.fromString)
          else List()
        val player = new Player(name)
        player.points = points
        player.hand = hand
        players :+= player

      case line if line.startsWith("TABLE:") =>
        val cardStrings = line.stripPrefix("TABLE:").split(",").toList.filter(_.nonEmpty)
        tableCards = cardStrings.map(PlayingCard.fromString)

      case line if line.startsWith("DECK:") =>
        val cardStrings = line.stripPrefix("DECK:").split(",").toList.filter(_.nonEmpty)
        deckCards = cardStrings.map(PlayingCard.fromString)

      case line if line.startsWith("TURN:") =>
        turnPlayerName = line.stripPrefix("TURN:")

      case _ =>
    }
    val game = new Game(players)
    game.board.tableCards = tableCards
    game.board.deck.setCards(deckCards)
    val index =players.indexWhere(_.name == turnPlayerName)
    if index != -1 then game.currentPlayerIndex = index
    game




