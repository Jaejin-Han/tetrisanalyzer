package nu.tengstrand.tetrisanalyzer.gui

import scala.swing._
import nu.tengstrand.tetrisanalyzer.game._
import actors.Actor._
import nu.tengstrand.tetrisanalyzer.settings.DefaultColorSettings

object TetrisAnalyzer extends SimpleSwingApplication {

  def top = new MainFrame {
    title = "Tetris Analyzer - by Joakim Tengstrand"

    preferredSize = new Dimension(700,550)

    val label = new Label {
      text = "testing"
    }

    val colorSettings = new DefaultColorSettings
    val gameView = new GameView(colorSettings)
    val timer = new Timer(this, gameView)
    val game = new Game(timer, gameView, gameView)

    actor {
      timer.start
    }

    contents = new BoxPanel(Orientation.Horizontal) {
      contents += gameView
    }

    gameView.preferredSize = new Dimension(500, 550)
    gameView.size = new Dimension(500, 550)

    new KeyManager(game)
  }
}