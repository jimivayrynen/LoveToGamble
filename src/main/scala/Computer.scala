class ComputerPlayer(name: String) extends Player(name):

  //yrittää ottaa mah monta kortia, mutta ei mökkiä
  def decideMove(tableCards: List[PlayingCard]): (PlayingCard, List[PlayingCard]) =

    val allMoves = this.hand.flatMap( card =>
      val combos = CardValidator.findValidTakes(card, tableCards)
      combos.map(c => (card, c)))

    // suodattaa mökit pois
    val noMokkiMoves = allMoves.filter(_._2.size < tableCards.size)

    if noMokkiMoves.nonEmpty then
      noMokkiMoves.maxBy(_._2.size)                               //siirto, jolla eniten kortteja
    else if allMoves.nonEmpty then
      allMoves.maxBy(_._2.size)                                   // jos kaikki mökkejä ottaa parhaan
    else
      val worstCard = this.hand.minBy(CardValueHelper.cardValue)  // jos ei voi ottaa mitään, antaa huonoimman kortin
      (worstCard, Nil)
