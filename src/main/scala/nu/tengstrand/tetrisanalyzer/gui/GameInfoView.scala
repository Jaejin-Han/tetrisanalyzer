package nu.tengstrand.tetrisanalyzer.gui

import nu.tengstrand.tetrisanalyzer.game.GameInfoReceiver
import java.awt._

case class SpeedInfo(secondsPassed: Double, piecesTotal: Long, clearedRowsTotal: Long)

class GameInfoView extends GameInfoReceiver {
  private var seed = 0L
  private var speed = ""
  private var slidingEnabled = false
  private var boardSize: Dimension = new Dimension(10, 20)
  private var pieces = 0L
  private var piecesTotal = 0L
  private var clearedRows = 0L
  private var clearedRowsTotal = 0L

  private var games = 0L
  private var minRows = 0L
  private var maxRows = 0L

  private var secondsPassed = 0.0
  private var speedInfo = SpeedInfo(0.0, 0, 0)

  private val textDrawer = new TextDrawer

  private val numberSeparator = new NumberSeparator

  // TODO: Refactor out
  private var paused = true

  def setSeed(seed: Long) { this.seed = seed }
  def setSliding(enabled: Boolean) { slidingEnabled = enabled }
  def setBoardSize(width: Int, height: Int) { boardSize = new Dimension(width, height) }
  def setNumberOfPieces(pieces: Long) { this.pieces = pieces }
  def setTotalNumberOfPieces(piecesTotal: Long) { this.piecesTotal = piecesTotal }
  def setNumberOfClearedRows(clearedRows: Long) { this.clearedRows = clearedRows }
  def setTotalNumberOfClearedRows(clearedRowsTotal: Long) { this.clearedRowsTotal = clearedRowsTotal }
  def setTimePassed(seconds: Double) { secondsPassed = seconds }
  def setPaused(paused: Boolean) { this.paused = paused }
  def updateGui() { /* repaint() */ }

  def setSpeed(name: String) {
    speed = name
    speedInfo = SpeedInfo(secondsPassed, piecesTotal, clearedRowsTotal)
  }

  def setNumberOfGamesAndRowsInLastGame(games: Long, rows: Long, totalClearedRows: Long, minRows: Long, maxRows: Long) {
    this.games = games
    this.minRows = minRows
    this.maxRows = maxRows
    this.clearedRowsTotal = totalClearedRows
  }

  def paintGraphics(origoX: Int, graphics: Graphics) {
    val g = graphics.asInstanceOf[Graphics2D];

    textDrawer.prepareDraw(origoX, g)

    textDrawer.drawInfo("Rows:", withSpaces(clearedRows), 1, g)
    textDrawer.drawInfo("Pieces:", withSpaces(pieces), 2, g)

    textDrawer.drawInfo("Rows total:", withSpaces(clearedRowsTotal), 4, g)
    textDrawer.drawInfo("Pieces total:", withSpaces(piecesTotal), 5, g)

    textDrawer.drawInfo("Games:", withSpaces(games), 7, g)
    textDrawer.drawInfo("Rows/game:", withSpaces(if (games == 0) 0 else clearedRowsTotal / games), 8, g)
    textDrawer.drawInfo("Min rows:", withSpaces(minRows), 9, g)
    textDrawer.drawInfo("Max rows:", withSpaces(maxRows), 10, g)

    textDrawer.drawInfo("Speed:", speed, 12, g)
    textDrawer.drawInfo("Rows/sec:", calculateUnitsPerSec(clearedRowsTotal - speedInfo.clearedRowsTotal), 13, g)
    textDrawer.drawInfo("Pieces/sec:", calculateUnitsPerSec(piecesTotal - speedInfo.piecesTotal), 14, g)

    textDrawer.drawInfo("Board:", boardSize.width + " x " + boardSize.height, 16, g)
    textDrawer.drawInfo("S[e]ed:", seed, 17, g)
    textDrawer.drawInfo("S[l]iding:", if (slidingEnabled) "On" else "Off", 18, g)

    textDrawer.drawInfo("[P]ause:", if (paused) "On" else "", 20, g)



    textDrawer.drawInfo("Elapsed time:", calculateElapsedTime(secondsPassed), 24, g)

    textDrawer.drawText("Press [F1] for help", 26, g)
  }

  private def withSpaces(number: Long) = numberSeparator.withSpaces(number)

  private def calculateElapsedTime(seconds: Double): String = {
    val sec: Int = (seconds*10 % 600).toInt
    val min: Int = (seconds/60 % 60).toInt
    val hours: Int = (seconds/3600).toInt
    hours + "h " + min + "m " + (sec/10) + "." + (sec%10) + "s"
  }

  private def calculateUnitsPerSec(total: Double): Any = {
    val seconds = secondsPassed - speedInfo.secondsPassed

    if (seconds == 0 || total == 0) {
      0
    } else {
      if (total / seconds >= 100)
        withSpaces(scala.math.round(total / seconds))
      else
        (scala.math.round(total * 10 / seconds) / 10.0)
    }
  }
}