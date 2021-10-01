/**
 * Copyright 2021 Isaac D. Griffith
 * <p>
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and
 * to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * <p>
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of
 * the Software.
 * <p>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR
 * COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package cs.isu.edu.cs3321.lightsout;

import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

import static cs.isu.edu.cs3321.lightsout.Constants.*;

/**
 * Class representing the current state of the Lights Out Game
 *
 * @author Isaac D. Griffith
 * @version 1.0.0
 */
public class GameState {

    @Getter
    private int[][] board;
    @Getter
    @Setter
    private boolean winner;

    /**
     * Constructor
     */
    public GameState() {
        board = new int[MAX_Y][MAX_X];
        winner = false;
    }

    /**
     * Flips the value at the coordinate x,y from 1 to 0 and vice versa
     *
     * @param x The column of the value to be flipped
     * @param y The row of the value to be flipped
     */
    void flip(int x, int y) {
        int val = get(y, x);
        if (val == 0) set(y, x, 1);
        else set(y, x, 0);
    }

    /**
     * Sets the value at the coordinate x, y to be the provided value
     *
     * @param y   The row of the value to be set
     * @param x   The column of the value to be set
     * @param val The new value
     */
    void set(int y, int x, int val) {
        if (val > 1) val = 1;
        if (val < 0) val = 0;
        //if (!validLocation(x, y)) return;
        board[y][x] = val;
    }

    /**
     * Returns the value at the coordinate y,x
     *
     * @param y The row of the value to retrieve
     * @param x The column of the value to retrieve
     * @return The value of the board at row y and column x
     */
    public int get(int y, int x) {
        return board[y][x];
    }

    /**
     * Tests if the provided location x,y is a valid location
     *
     * @param x the column to be tested
     * @param y the row to be tested
     * @return True if the column and row are within bounds
     */
    boolean validLocation(int x, int y) {
        return x >= 0 && x < MAX_X && y >= 0 && y < MAX_Y;
    }
}
