import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{HBox, Pane, VBox}
import scalafx.scene.shape.Rectangle
import scalafx.scene.paint.Color.*
import scalafx.scene.text.{Font, Text}
import scalafx.event.EventIncludes.*
import scalafx.scene.control.Label
import scalafx.scene.image.ImageView
import scalafx.scene.image.Image
import scalafx.scene.paint.Color
import scalafx.util.Duration
import scalafx.scene.effect.DropShadow
import scalafx.animation.TranslateTransition
import javafx.event.EventHandler
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.language.postfixOps

object Main extends JFXApp3:

  val mainRoot = new Pane():
    style = "-fx-background-color: darkgreen"
  val game = new Game(List(new Player("Player 1"), new Player("Player 2")))

  // käynnistää pelin ja alustaa pelinäkymän
  def start() =

    stage = new JFXApp3.PrimaryStage:
      title = "Mökki Kasino"
      width = 900
      height = 650
      scene = new Scene:
        root = mainRoot

    game.startGame()
    updateUI()

  // pävittää pelinäkymän tilanteen mukaan
  def updateUI(): Unit =
      mainRoot.children.clear()

      val tableArea = new Rectangle:
        x = 100
        y = 50
        width = 600
        height = 200
        fill = DarkOliveGreen
        arcWidth = 20
        arcHeight = 20

    // pelaaja info
      val playerInfo = new VBox:
        layoutX = 20
        layoutY = 20
        spacing = 10
        children = Seq(
          new Label(s"Vuorossa: ${game.currentPlayer.name}"):
            font = Font(20)
            textFill = White,
          new Label(s"Pisteet: ${game.currentPlayer.points}"):
            font = Font(18)
            textFill = Yellow,
          new Label(s"Kortteja jäljellä: ${game.deck.cardsLeft}"):
            font = Font(16)
            textFill = White
            )
      mainRoot.children ++= Seq(tableArea, playerInfo)

      // Näytetään pelaajien pisteet
      val playerScores = new HBox:
        layoutX = 100
        layoutY = 420
        spacing = 50
        children = game.players.map(p =>
          new Label(s"${p.name}: ${p.points} pistettä"):
            font = Font(16)
            textFill = LightGray)

      mainRoot.children += playerScores

    // Näyttää voitto ilmoituksen
      if game.isGameOver then
        val winText = new Text(300, 250, s"${game.winner} voitti pelin!"):
          font = Font(30)
          fill = Gold
          effect = new DropShadow(radius = 10, color = Black)
        mainRoot.children = winText
      else renderCards()


  // piirtää pelaajien ja pöydällä olevien kortit
  def renderCards(): Unit =
        game.currentPlayer.hand.zipWithIndex.foreach( (card, i) =>
          val cardView = createCardImageView(200 + i * 80, 500, card)
          cardView.onMouseEntered = _ => cardView.translateY = -10
          cardView.onMouseExited = _ => cardView.translateY = 0

          cardView.onMouseClicked = _ => handleCardClick(card, cardView)

          mainRoot.children += cardView)

        game.board.tableCards.zipWithIndex.foreach((card, i) =>
          val cardView = createCardImageView(150 + i * 40, 100, card)
          mainRoot.children += cardView)

  // Käsittelee kortin klikkauksen ja suorittaa siirron + päivittää näkymää viivellä
  def handleCardClick(card: PlayingCard, cardView: ImageView): Unit =
    if game.playCard(card) then
      animateCardMove(cardView)
      Future:
        Thread.sleep(350)
        javafx.application.Platform.runLater(() => updateUI())

  //Animaatio, missä kortti nousee hiiren siihen osuessa
  def animateCardMove(cardView: ImageView): Unit =
    val animation = new TranslateTransition(Duration(300), cardView)
    animation.byY = -200
    animation.play()


  //Luo kortin kuvaesityksen GUI:hin annetuilla sijainneilla
  def createCardImageView(xPos: Double, yPos: Double, card: PlayingCard): ImageView =
    val path = s"/cards/${card.imagePath}"
    val stream = getClass.getResourceAsStream(s"/cards/${card.imagePath}")

    if stream == null then
    throw new RuntimeException(s"Kuvaa ei löytynyt: $path")

    val image = new Image(stream)
    new ImageView(image):
      fitWidth = 50
      fitWidth = 70
      preserveRatio = true
      this.x = xPos
      this.y = yPos

  //Näyttää hetkellisen virheilmoituksen
  def showTemoraryMessage(text: String, color: Color): Unit =
    val msg = new Label(text):
      layoutX = 350
      layoutY = 350
      font = Font(20)
      textFill = color
      effect = new DropShadow(radius = 10, color = Black)

    mainRoot.children += msg

    val fadeOut = new javafx.animation.FadeTransition(javafx.util.Duration.seconds(2), msg)
    fadeOut.setFromValue(1.0)
    fadeOut.setToValue(0.0)
    fadeOut.setOnFinished(_ => mainRoot.children -= msg)
    fadeOut.play()


end Main

