package com.github.tetrisanalyzer.gui

import swing._
import com.github.tetrisanalyzer.game.{PauseMessage, GameInfoReceiver}

class GameInfoView extends FlowPanel with GameInfoReceiver {
  val pause = new Label("On")
  val pieces = new Label("0")
  val lines = new Label("0")

  val numberSeparator = new NumberSeparator

  def setPause(pause: Boolean) { this.pause.text = if (pause) "On" else "Off" }
  def setPieces(pieces: Long) { this.pieces.text = numberSeparator.withSpaces(pieces) }
  def setTotalClearedLines(lines: Long) { this.lines.text = numberSeparator.withSpaces(lines) }

  def gameInfoReceived(message: PauseMessage) {
    repaint()
  }

  contents += new Label("[P]aus: "); contents += pause
  contents += new Label("Moves: "); contents += pieces
  contents += new Label("Lines: "); contents += lines

  maximumSize = new Dimension(100,500)
}