package ru.spbau.mit.karvozavr.tictactoe.core;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.*;

public class GameFieldTest {

    @Test
    public void testInitField() {
        GameField field = new GameField();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                assertThat(field.getCell(i, j), is(nullValue()));
            }
        }
    }

    @Test
    public void testSet() {
        GameField field = new GameField();
        boolean flag = true;
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                field.setCell(i, j, flag ? CellType.X : CellType.O);
                assertThat(field.getCell(i, j), is(flag ? CellType.X : CellType.O));
                flag = !flag;
            }
        }
    }

    // Test Diagonal

    @Test
    public void testDiagonalWinX0() {
        GameField field = new GameField();
        field.setCell(0, 0, CellType.X);
        field.setCell(1, 1, CellType.X);
        field.setCell(2, 2, CellType.X);

        assertThat(field.checkForGameEnd(), is(GameResult.X_WIN));
    }

    @Test
    public void testDiagonalWinX1() {
        GameField field = new GameField();
        field.setCell(0, 2, CellType.X);
        field.setCell(1, 1, CellType.X);
        field.setCell(2, 0, CellType.X);

        assertThat(field.checkForGameEnd(), is(GameResult.X_WIN));
    }

    @Test
    public void testDiagonalWinO0() {
        GameField field = new GameField();
        field.setCell(0, 0, CellType.O);
        field.setCell(1, 1, CellType.O);
        field.setCell(2, 2, CellType.O);

        assertThat(field.checkForGameEnd(), is(GameResult.O_WIN));
    }

    @Test
    public void testDiagonalWinO1() {
        GameField field = new GameField();
        field.setCell(0, 2, CellType.O);
        field.setCell(1, 1, CellType.O);
        field.setCell(2, 0, CellType.O);

        assertThat(field.checkForGameEnd(), is(GameResult.O_WIN));
    }


    // Vertical

    @Test
    public void testVerticalWinO0() {
        GameField field = new GameField();
        field.setCell(0, 0, CellType.X);
        field.setCell(1, 0, CellType.X);
        field.setCell(2, 0, CellType.X);

        assertThat(field.checkForGameEnd(), is(GameResult.X_WIN));
    }

    @Test
    public void testVerticalWinO1() {
        GameField field = new GameField();
        field.setCell(0, 1, CellType.X);
        field.setCell(1, 1, CellType.X);
        field.setCell(2, 1, CellType.X);

        assertThat(field.checkForGameEnd(), is(GameResult.X_WIN));
    }

    @Test
    public void testVerticalWinO2() {
        GameField field = new GameField();
        field.setCell(0, 2, CellType.X);
        field.setCell(1, 2, CellType.X);
        field.setCell(2, 2, CellType.X);

        assertThat(field.checkForGameEnd(), is(GameResult.X_WIN));
    }

    // Horizontal

    @Test
    public void testHorizontalWinO0() {
        GameField field = new GameField();
        field.setCell(0, 0, CellType.X);
        field.setCell(0, 1, CellType.X);
        field.setCell(0, 2, CellType.X);

        assertThat(field.checkForGameEnd(), is(GameResult.X_WIN));
    }

    @Test
    public void testHorizontalWinO1() {
        GameField field = new GameField();
        field.setCell(1, 0, CellType.X);
        field.setCell(1, 1, CellType.X);
        field.setCell(1, 2, CellType.X);

        assertThat(field.checkForGameEnd(), is(GameResult.X_WIN));
    }

    @Test
    public void testHorizontalWinO2() {
        GameField field = new GameField();
        field.setCell(2, 0, CellType.X);
        field.setCell(2, 1, CellType.X);
        field.setCell(2, 2, CellType.X);

        assertThat(field.checkForGameEnd(), is(GameResult.X_WIN));
    }

    // Test draw

    @Test
    public void testDraw0() {
        GameField field = new GameField();
        field.setCell(0, 0, CellType.X);
        field.setCell(0, 1, CellType.O);
        field.setCell(0, 2, CellType.O);

        field.setCell(1, 0, CellType.O);
        field.setCell(1, 1, CellType.X);
        field.setCell(1, 2, CellType.X);

        field.setCell(2, 0, CellType.X);
        field.setCell(2, 1, CellType.X);
        field.setCell(2, 2, CellType.O);

        assertThat(field.checkForGameEnd(), is(GameResult.DRAW));
    }

    @Test
    public void testDraw1() {
        GameField field = new GameField();
        field.setCell(0, 0, CellType.O);
        field.setCell(0, 1, CellType.X);
        field.setCell(0, 2, CellType.X);

        field.setCell(1, 0, CellType.X);
        field.setCell(1, 1, CellType.O);
        field.setCell(1, 2, CellType.O);

        field.setCell(2, 0, CellType.X);
        field.setCell(2, 1, CellType.O);
        field.setCell(2, 2, CellType.X);

        assertThat(field.checkForGameEnd(), is(GameResult.DRAW));
    }

    @Test
    public void testGameNotFinished() {
        GameField field = new GameField();
        field.setCell(0, 2, CellType.X);
        field.setCell(1, 0, CellType.X);
        field.setCell(1, 1, CellType.O);
        field.setCell(2, 1, CellType.O);
        field.setCell(2, 2, CellType.X);

        assertThat(field.checkForGameEnd(), is(nullValue()));
    }
}