package com.github.tetrisanalyzer.piecemove;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.AtariGameSettings;
import com.github.tetrisanalyzer.settings.GameSettings;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static com.github.tetrisanalyzer.piece.Piece.createPieceO;
import static org.junit.Assert.assertEquals;

public class ValidPieceMovesTest {

    @Test
    public void getStartMove() {
        GameSettings settings = new AtariGameSettings(6, 5);
        Board board = Board.create(settings.boardWidth, settings.boardHeight);
        Piece piece = createPieceO(settings);
        ValidPieceMovesForEmptyBoard validPieceMoves = new ValidPieceMovesForEmptyBoard(board, piece, settings);
        PieceMove startMove = validPieceMoves.calculateStartMove();

        assertEquals(new PieceMove(board, piece, new Move(0,1, 0)), startMove);

        Set<PieceMove> expectedMoves = new HashSet<>();
        expectedMoves.add(new PieceMove(board, piece, new Move(0,0, 0)));
        expectedMoves.add(new PieceMove(board, piece, new Move(0,2, 0)));

        assertEquals(expectedMoves, startMove.asideAndRotateMoves());

        assertEquals(new PieceMove(board, piece, new Move(0,1, 1)), startMove.down);
    }
}
