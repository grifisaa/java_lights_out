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
import java.util.Random;

import static cs.isu.edu.cs3321.lightsout.Constants.MAX_X;
import static cs.isu.edu.cs3321.lightsout.Constants.MAX_Y;

/**
 * Simple class implementing the rules for the LightsOut Game
 *
 * @author Isaac D. Griffith
 * @version 1.0.0
 */
public class Game {

    @Getter
    @Setter
    private GameState state;

    /**
     * Constructor
     */
    public Game() {
        state = new GameState();
        reset();
    }

    /**
     * Updates the gameboard for a selection of the item at the coordinate x, y
     *
     * @param x The column in the board, which was selected
     * @param y The row in the board, which was selected
     */
    public void update(int x, int y) {
        List<Integer> xChange = Lists.newArrayList(-1, 1);
        List<Integer> yChange = Lists.newArrayList(-1, 1);

        for (int xVal : xChange) {
            try {
                state.flip(x + xVal, y);
            } catch (ArrayIndexOutOfBoundsException ex) {

            }
        }
        for (int yVal : yChange) {
            try {
                state.flip(x, y + yVal);
            } catch (ArrayIndexOutOfBoundsException ex) {

            }
        }
        state.flip(x, y);
    }

    /**
     * Resets the game board to a random state
     */
    public void reset() {
        Random rand = new Random();

        for (int i = 0; i < MAX_X; i++) {
            for (int j = 0; j < MAX_Y; j++) {
                state.set(i, j, rand.nextBoolean() ? 1 : 0);
            }
        }

        state.setWinner(false);
    }

    /**
     * Detects if the game board is in a winning state
     *
     * @return true if no value of the board is a 1 or not, otherwise falase
     */
    public boolean checkWinner() {
        for (int i = 0; i < MAX_X; i++) {
            for (int j = 0; j < MAX_Y; j++) {
                if (state.get(i, j) == 1)
                    return false;
            }
        }
        return true;
    }
}
