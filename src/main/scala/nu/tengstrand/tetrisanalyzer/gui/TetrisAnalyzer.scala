package nu.tengstrand.tetrisanalyzer.gui

import scala.swing._
import nu.tengstrand.tetrisanalyzer.board.Board
import nu.tengstrand.tetrisanalyzer.boardevaluator.JTengstrandBoardEvaluator1
import nu.tengstrand.tetrisanalyzer.piecegenerator.DefaultPieceGenerator
import nu.tengstrand.tetrisanalyzer.settings.DefaultGameSettings
import nu.tengstrand.tetrisanalyzer.game._
import actors.Actor._

object TetrisAnalyzer extends SimpleSwingApplication {

  def top = new MainFrame {
    title = "Tetris Analyzer - by Joakim Tengstrand"

    preferredSize = new Dimension(650,550)

    val label = new Label {
      text = "testing"
    }

    val board = Board()
    val boardEvaluator = new JTengstrandBoardEvaluator1(board.width, board.height)
    val settings = new DefaultGameSettings
    val pieceGenerator = new DefaultPieceGenerator(settings.pieceGeneratorSeed)
    val position = Position()
    val positionView = new PositionView(settings)
    val gameInfoView = new GameInfoView()
    val gameEventReceiver = new GameEventDelegate(positionView, gameInfoView)

    val computerPlayer = new ComputerPlayer(board, position, boardEvaluator, pieceGenerator, settings, gameEventReceiver)

    contents = new BoxPanel(Orientation.Horizontal) {
      contents += positionView
      contents += gameInfoView
    }

//    positionView.background = new Color(0,255,0)
//    gameInfoView.background = new Color(255,0,0)

    positionView.preferredSize = new Dimension(500, 550)
    positionView.size = new Dimension(500, 550)

    gameInfoView.preferredSize = new Dimension(150, 550)
    gameInfoView.size = new Dimension(150, 550)

    computerPlayer.start

    val timer = new Timer(this, gameInfoView)

    new KeyManager(computerPlayer, positionView, gameEventReceiver, timer)

    actor {
      timer.start
    }
  }
}