package com.github.tetrisanalyzer.move;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.piecemove.AllValidPieceMovesForEmptyBoard;
import com.github.tetrisanalyzer.piecemove.PieceMove;
import com.github.tetrisanalyzer.settings.GameSettings;
import com.github.tetrisanalyzer.settings.TetrisAnalyzerGameSettings;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.github.tetrisanalyzer.board.Board.createBoard;
import static com.github.tetrisanalyzer.piece.Piece.createPieceS;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

public class ValidMovesTest {

    @Test
    public void pieceMoves() {
        GameSettings settings = new TetrisAnalyzerGameSettings(5, 4);
        Board board = createBoard(settings.boardWidth(), settings.boardHeight());
        ValidMoves validMoves = new ValidMoves(board);
        PieceMove startPiece = getStartPieceS(board, settings);
        List<Move> moves = new ArrayList<Move>();

        for (PieceMove pieceMove : validMoves.pieceMoves(startPiece)) {
            moves.add(pieceMove.move);
        }

        assertEquals(7, moves.size());
        assertTrue(moves.contains(new Move(0,0, 2)));
        assertTrue(moves.contains(new Move(0,1, 2)));
        assertTrue(moves.contains(new Move(0,2, 2)));
        assertTrue(moves.contains(new Move(1,0, 1)));
        assertTrue(moves.contains(new Move(1,1, 1)));
        assertTrue(moves.contains(new Move(1,2, 1)));
        assertTrue(moves.contains(new Move(1,3, 1)));
    }

    private PieceMove getStartPieceS(Board board, GameSettings settings) {
        Piece piece = createPieceS(settings);
        AllValidPieceMovesForEmptyBoard allValidPieceMoves = new AllValidPieceMovesForEmptyBoard(board, settings);

        return allValidPieceMoves.startMoveForPiece(piece);
    }
}
