import scalafx.application.JFXApp3
import scalafx.scene.Scene
import scalafx.scene.input.MouseEvent
import scalafx.scene.layout.{HBox, Pane, VBox}
import scalafx.scene.shape.Rectangle
import scalafx.scene.paint.Color.*
import scalafx.scene.text.{Font, Text}
import scalafx.event.EventIncludes.*
import scalafx.scene.control.{Button, ComboBox, Label, TextField}
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
  var game: Game = null

  var selectedHandCard: Option[PlayingCard] = None
  var selectedTableCards: Set[PlayingCard] = Set()

  // käynnistää pelin ja alustaa pelinäkymän
  def start() =

    stage = new JFXApp3.PrimaryStage:
      title = "Mökki Kasino"
      width = 900
      height = 650
      scene = new Scene:
        root = mainRoot

    showStartMenu()

  def showStartMenu(): Unit =
    mainRoot.children.clear()

    val title = new Text("Mökki Kasino"):
      font = Font(32)
      fill = Gold
      layoutX = 330
      layoutY = 50

    val playerCountBox = new ComboBox[Int](Seq(2, 3, 4)):
      value = 2
      layoutX = 400
      layoutY = 100

    val nameFields = new VBox:
      layoutX = 360
      layoutY = 150
      spacing = 10

    def updateNameFields(count: Int): Unit =
      nameFields.children = (1 to count).map( i => new TextField {promptText = s"Pelaaja $i"})

    updateNameFields(playerCountBox.value())
    playerCountBox.onAction = _ => updateNameFields(playerCountBox.value())

    val startButton = new Button("Aloita peli"):
      layoutX = 420
      layoutY = 320
      onAction = _ =>
        val names = nameFields.children.toList.collect { case node: javafx.scene.control.TextField => node.getText}.filter(_.nonEmpty)
        if names.size == playerCountBox.value() then
          val players = names.map(n => new Player(n)).toList
          game = new Game(players)
          game.startGame()
          updateUI()
        else
          showTemoraryMessage("Syötä kaikkien nimet", Color.Red)

    mainRoot.children ++= Seq(title, playerCountBox, nameFields, startButton)


  def menuBox() = new VBox:
      layoutX = 750
      layoutY = 20
      spacing = 10
      children = Seq(
        new Button("Tallenna peli"):
          onAction = _ => FileHandler.saveGame(game, "tallennus.txt"),

        new Button("Lataa peli"):
          onAction = _ =>
            val loaded = FileHandler.loadGame("tallennus.txt")
            game.copyFrom(loaded)
            updateUI())


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
          new Label(s"Kortteja jäljellä: ${game.board.deck.cardsLeft}"):
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
          new Label(s"${p.name}: ${p.points} pistettä, ${p.mokkiCount} mökkiä"):
            font = Font(16)
            textFill = LightGray)

      mainRoot.children += playerScores


    // Näyttää voitto ilmoituksen
      if game.isGameOver then
        game.endGame()
        val winText = new Text(300, 250, s"${game.winner} voitti pelin!"):
          font = Font(30)
          fill = Gold
          effect = new DropShadow(radius = 10, color = Black)

        val newGameButton = new Button("Uusi peli?"):
          layoutX = 400
          layoutY = 300
          onAction = _ => showStartMenu()

        mainRoot.children ++ Seq(winText, newGameButton)
      else renderCards()

      mainRoot.children += menuBox()


  // piirtää pelaajien ja pöydällä olevien kortit
  def renderCards(): Unit =
    //käden kortin valinta
    game.currentPlayer.hand.zipWithIndex.foreach( (card, i) =>
      val cardView = createCardImageView(200 + i * 80, 500, card)
      cardView.onMouseEntered = _ => cardView.translateY = -10
      cardView.onMouseExited = _ => cardView.translateY = 0

      if selectedHandCard.contains(card) then
        cardView.effect = new DropShadow(10, LightBlue)

      cardView.onMouseClicked = _ =>
        selectedHandCard = Some(card)
        updateUI()
      mainRoot.children += cardView)

    //pöytäkorttien valinta
    game.board.tableCards.zipWithIndex.foreach( (card, i) =>
      val cardView = createCardImageView(150 + i *40, 100, card)

      if selectedTableCards.contains(card) then
        cardView.effect = new DropShadow(10, Yellow)

      cardView.onMouseClicked = _ =>
        if selectedTableCards.contains(card) then
          selectedTableCards -= card
        else
          selectedTableCards += card
        updateUI()
      mainRoot.children += cardView)

    //pelaa nappi, toimii jos käden kortti on valittu
    val playButton = new Button("Pelaa"):
      layoutX = 400
      layoutY = 580
      disable = selectedHandCard.isEmpty
      onAction = _ =>
        selectedHandCard.foreach( handcard =>
          val succes = game.playCard(handcard, selectedTableCards.toList)
          if succes then
            selectedHandCard = None
            selectedTableCards = Set()
            game.switchTurn()
            updateUI()
          else
            showTemoraryMessage("Virheellinen siirto!", Color.Red))

    mainRoot.children += playButton


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

