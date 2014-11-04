package com.github.tetrisanalyzer.board;

import com.github.tetrisanalyzer.move.Move;
import com.github.tetrisanalyzer.piece.Piece;
import com.github.tetrisanalyzer.settings.StandardGameSettings;
import org.junit.Test;

import static com.github.tetrisanalyzer.piece.Piece.createPieceZ;
import static junit.framework.Assert.assertEquals;
import static org.junit.Assert.*;

public class BoardTest {

    @Test(expected = IllegalArgumentException.class)
    public void tooLow() {
        Board.create(
                "|----------|",
                "|----x-----|",
                "|-x--x----x|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooNarrow() {
        Board.create(
                "|---|",
                "|---|",
                "|---|",
                "|-x-|",
                "ŻŻŻŻŻ");
    }

    @Test(expected = IllegalArgumentException.class)
    public void tooWide() {
        Board.create(33, 10);
    }

    @Test
    public void testToString() {
        String[] expectedBoard = new String[]{
                "|----------|",
                "|----------|",
                "|----x-----|",
                "|-x--x----x|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ"};

        Board board = Board.create(expectedBoard);

        assertEquals(asBoard(expectedBoard), board.toString());
    }

    private String asBoard(String[] boardArray) {
        String board = "";
        String separator = "";
        for (int y = 0; y < boardArray.length; y++) {
            board += separator + boardArray[y];
            separator = "\n";
        }
        return board;
    }

    @Test
    public void isFree_occupied() {
        Board board = Board.create(
                "|---------|",
                "|---------|",
                "|---------|",
                "|---------|",
                "|-x-------|",
                "ŻŻŻŻŻŻŻŻŻŻŻ");

        assertFalse(board.isFree(1, 4));
    }

    @Test
    public void isFree() {
        Board board = Board.create(
                "|xxxxxxxxx|",
                "|xxxxxxxxx|",
                "|xxxxxxxxx|",
                "|xxxxxxxxx|",
                "|x-xxxxxxx|",
                "ŻŻŻŻŻŻŻŻŻŻŻ");

        assertTrue(board.isFree(1, 4));
    }

    @Test
    public void clearRows() {
        Board board = Board.create(
                "|----------|",
                "|----x-----|",
                "|xxxxxxxxxx|",
                "|xxxxxxxxxx|",
                "|-x--x----x|",
                "|xxxxxxxxxx|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ");

        assertEquals(3, board.clearRows(2, 4));

        assertEquals(Board.create(
                "|----------|",
                "|----------|",
                "|----------|",
                "|----------|",
                "|----x-----|",
                "|-x--x----x|",
                "ŻŻŻŻŻŻŻŻŻŻŻŻ"), board);
    }

    @Test
    public void copy() {
        Board board = board();

        assertEquals(board, board.copy());
    }

    @Test
    public void restore() {
        Board empty = Board.create(8, 4);
        Board copy = board();
        empty.restore(copy);

        assertEquals(copy, empty);
    }

    @Test
    public void cantHaveOddNumberOfCellsOnAnEvenBoardWidth() {
        try {
            Board.create(
                    "|------|",
                    "|------|",
                    "|x-----|",
                    "|xx----|",
                    "ŻŻŻŻŻŻŻŻ");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void canHaveOddNumberOfCellsOnAnOddBoardWidth() {
        Board.create(
                "|-----|",
                "|-----|",
                "|x----|",
                "|xx---|",
                "ŻŻŻŻŻŻŻ");
    }

    @Test
    public void asString() {
        Board board = Board.create(
                "|--------|",
                "|--------|",
                "|--------|",
                "|--------|",
                "|x------x|",
                "ŻŻŻŻŻŻŻŻŻŻ");

        Piece piece = createPieceZ(new StandardGameSettings(board));
        Move move = new Move(0,0, 3);

        assertEquals(" Z: 0,1\n" + board(
                "|--------|",
                "|--------|",
                "|--------|",
                "|--------|",
                "|x------x|",
                "ŻŻŻŻŻŻŻŻŻŻ"), board.asString(piece, move));
    }

    @Test
    public void asString_noValidMove() {
        Board board = Board.create(
                "|--------|",
                "|--xxx---|",
                "|xxxxx-xx|",
                "|xx-xxxxx|",
                "|xxxxxx-x|",
                "ŻŻŻŻŻŻŻŻŻŻ");

        Piece piece = createPieceZ(new StandardGameSettings(board));
        Move move = null;

        assertEquals(" Z: -\n" + board(
                "|--------|",
                "|--xxx---|",
                "|xxxxx-xx|",
                "|xx-xxxxx|",
                "|xxxxxx-x|",
                "ŻŻŻŻŻŻŻŻŻŻ"), board.asString(piece, move));
    }

    @Test
    public void createJunkBoard_oddWidth() {
        assertEquals(Board.create(
                "|x-x-x-x|",
                "|-x-x-x-|",
                "|x-x-x-x|",
                "|-x-x-x-|",
                "|x-x-x-x|",
                "|-x-x-x-|",
                "ŻŻŻŻŻŻŻŻŻ"), Board.createChessBoard(7, 6));
    }

    @Test
    public void createJunkBoard_evenWidth() {
        assertEquals(Board.create(
                "|x-x-x-x-|",
                "|-x-x-x-x|",
                "|x-x-x-x-|",
                "|-x-x-x-x|",
                "|x-x-x-x-|",
                "|-x-x-x-x|",
                "ŻŻŻŻŻŻŻŻŻŻ"), Board.createChessBoard(8, 6));
    }

    private Board board() {
        return Board.create(
                "|x-------|",
                "|x-------|",
                "|xxx----x|",
                "|xxx-x-xx|",
                "ŻŻŻŻŻŻŻŻŻŻ");
    }

    private String board(String... rows) {
        String result = "";
        String newline = "";

        for (String row : rows) {
            result += newline + row;
            newline = "\n";
        }
        return result;
    }

}
