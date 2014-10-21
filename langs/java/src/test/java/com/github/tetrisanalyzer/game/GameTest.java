package com.github.tetrisanalyzer.game;

import com.github.tetrisanalyzer.board.ColoredBoard;
import com.github.tetrisanalyzer.boardevaluator.BoardEvaluator;
import com.github.tetrisanalyzer.boardevaluator.TengstrandBoardEvaluator1;
import com.github.tetrisanalyzer.piecegenerator.PieceGenerator;
import com.github.tetrisanalyzer.piecegenerator.PredictablePieceGenerator;
import com.github.tetrisanalyzer.settings.GameSettings;
import com.github.tetrisanalyzer.settings.TetrisAnalyzerGameSettings;
import org.junit.Test;

import static com.github.tetrisanalyzer.board.Board.createBoard;
import static junit.framework.Assert.assertEquals;

public class GameTest {

    @Test
    public void playFivePieces() {
        ColoredBoard board = ColoredBoard.create(10, 15);
        GameSettings settings = new TetrisAnalyzerGameSettings(true);
        BoardEvaluator boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height);
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(settings, "OLIZT");
        GameResult result = new GameResult(board, pieceGenerator, 5);
        Game game = new Game(result, boardEvaluator, settings);
        game.play();

        assertEquals(1, result.rows);

        assertEquals(createBoard(
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|---------Z|",
                "|--------ZZ|",
                "|OO---TTTZL|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ"), game.board);
    }

    @Test
    public void slidePiece() {
        ColoredBoard board = ColoredBoard.create(
                "|----------|",
                "|----------|",
                "|----------|",
                "|--------OO|",
                "|S--------Z|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ");
        GameSettings settings = new TetrisAnalyzerGameSettings(true);
        BoardEvaluator boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height);
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(settings, "T");
        GameResult result = new GameResult(board, pieceGenerator, 1);
        Game game = new Game(result, boardEvaluator, settings);
        game.play();

        assertEquals(ColoredBoard.create(
                "|----------|",
                "|----------|",
                "|----------|",
                "|-------TOO|",
                "|S-----TTTZ|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ"), game.coloredBoard);
    }

    @Test
    public void dropPieceWhenSlidingIsDisabled() {
        ColoredBoard board = ColoredBoard.create(
                "|----------|",
                "|----------|",
                "|----------|",
                "|--------OO|",
                "|S--------Z|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ");
        GameSettings settings = new TetrisAnalyzerGameSettings();
        BoardEvaluator boardEvaluator = new TengstrandBoardEvaluator1(board.width, board.height);
        PieceGenerator pieceGenerator = new PredictablePieceGenerator(settings, "T");
        GameResult result = new GameResult(board, pieceGenerator, 1);
        Game game = new Game(result, boardEvaluator, settings);
        game.play();

        assertEquals(ColoredBoard.create(
                "|----------|",
                "|----------|",
                "|----------|",
                "|TTT-----OO|",
                "|ST-------Z|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ"), game.coloredBoard);
    }

    // 1 000 000 = 200 sec = 5 000 validPieces/sec (sliding on)
    // 1 000 000 = 63 sec = 15 800 validPieces/sec (sliding off)
}
