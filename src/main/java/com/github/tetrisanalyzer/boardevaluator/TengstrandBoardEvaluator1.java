package com.github.tetrisanalyzer.boardevaluator;

import com.github.tetrisanalyzer.board.Board;
import com.github.tetrisanalyzer.board.BoardOutline;

public class TengstrandBoardEvaluator1 implements BoardEvaluator {
    private int boardWidth;
    private int boardHeight;

    private double[] heightFactor = new double[] { 7, 7, 2.5, 2.2, 1.8, 1.3, 1.0, 0.9, 0.7, 0.6, 0.5, 0.4, 0.3, 0.25, 0.2, 0.15, 0.1, 0.1, 0.1, 0.1, 0.1 };
    private double[] blocksPerLineHollowFactor = new double[] { 0, 0, 0, 0.1, 0.2, 0.3, 0.4, 0.5, 0.6, 0.553 };
    private double[] areaWidthFactor = new double[] { 0, 4.25, 2.39, 3.1, 2.21, 2.05, 1.87, 1.52, 1.34, 1.18, 0 };
    private double[] areaHeightFactor = new double[] { 0, .5, 1.19, 2.3, 3.1, 4.6, 5.6, 6.6, 7.6, 8.6, 9.6, 10.6, 11.6, 12.6, 13.6, 14.6, 15.6, 16.6, 17.6, 18.6, 19.6 };
    private double[] areaHeightFactorEqualWallHeight = new double[] { 0, .42, 1.05, 2.2, 3.1, 4.6, 5.6, 6.6, 7.6, 8.6, 9.6, 10.6, 11.6, 12.6, 13.6, 14.6, 15.6, 16.6, 17.6, 18.6, 19.6 };

    public TengstrandBoardEvaluator1() {
        this(10, 20);
    }

    public TengstrandBoardEvaluator1(int boardWidth, int boardHeight) {
        if (boardWidth > 10) {
            throw new IllegalArgumentException("Only board widths between 4 and 10 is supported at the moment, but was: " + boardWidth);
        }
        if (boardHeight > 20) {
            throw new IllegalArgumentException("Only board heights between 4 and 20 is supported at the moment, but was: " + boardHeight);
        }
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
    }

    public double evaluate(Board board)  {
        if (boardWidth > boardWidth) {
            throw new IllegalArgumentException("Can not evaluate board width > " + boardWidth);
        }
        BoardOutline outline = new BoardOutline(board);

        return evaluateBasedOnHollows(board, outline) +
            evaluateBasedOnOutlineHeight(outline) +
            evaluateBasedOnOutlineStructure(outline);
    }

    private double evaluateBasedOnHollows(Board board, BoardOutline outline) {
        double equity = 0;
        double[] hollowFactorForLine = new double[boardHeight + 1];

        for (int y=outline.getMinY(); y<boardHeight; y++) {
            int numberOfBlocksPerLine = 0;
            int minOutlineForHole = boardHeight;

            for (int x=0; x<boardWidth; x++) {
                if (!board.isFree(x, y)) {
                    numberOfBlocksPerLine++;
                } else if (outline.get(x) < minOutlineForHole && outline.get(x) < y) {
                    minOutlineForHole = outline.get(x);
                }
            }
            hollowFactorForLine[y] = blocksPerLineHollowFactor[numberOfBlocksPerLine];

            if (minOutlineForHole < boardHeight) {
                double hollowFactor = 1;

                for (int line=minOutlineForHole; line<=y; line++) {
                    hollowFactor *= hollowFactorForLine[line];
                }
                equity += (1 - hollowFactor) * boardWidth;
            }
        }
        return equity;
    }

    private double evaluateBasedOnOutlineHeight(BoardOutline outline) {
        double sum = 0;

        for (int x=0; x<boardWidth; x++) {
            sum += heightFactor[outline.get(x)];
        }
        return sum;
    }

    private double evaluateBasedOnOutlineStructure(BoardOutline outline) {
        double equity = 0;

        for (int x=1; x<=boardWidth; x++) {
            boolean hasAreaWallsSameHeight = false;
            boolean isAreaWallsSameHeightNotInitialized = true;
            int areaHeight = 0;
            int previousAreaWidth = 0;

            int rightWallY = outline.get(x);
            int startY = (x == boardWidth) ? outline.getMinY() : outline.get(x);

            // Calculate the size of the closed area in the outline (areaWidth * areaHeight).
            for (int y=startY; y<=boardHeight; y++) {
                int areaWidth = 0;

                for (int areaX=x-1; areaX>=0; areaX--) {
                    if (outline.get(areaX) <= y) {
                        if (isAreaWallsSameHeightNotInitialized) {
                            hasAreaWallsSameHeight = outline.get(areaX) == rightWallY;
                            isAreaWallsSameHeightNotInitialized = false;
                        }
                        break;
                    }
                    areaWidth++;
                }
                if (areaWidth == 0 && previousAreaWidth == 0) {
                    break;
                } else {
                    if (areaWidth > 0 && (areaWidth == previousAreaWidth || previousAreaWidth == 0)) {
                        areaHeight++;
                    } else {
                        if (hasAreaWallsSameHeight) {
                            equity += areaWidthFactor[previousAreaWidth] * areaHeightFactorEqualWallHeight[areaHeight];
                        } else {
                            equity += areaWidthFactor[previousAreaWidth] * areaHeightFactor[areaHeight];
                        }
                        areaHeight = 1;
                        isAreaWallsSameHeightNotInitialized = true;
                    }
                    previousAreaWidth = areaWidth;
                }
            }
        }
        return equity;
    }
}