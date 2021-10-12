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
package cs.isu.edu.cs3321.client;

import com.google.common.collect.Lists;
import cs.isu.edu.cs3321.lightsout.GameState;
import javafx.event.ActionEvent;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;

import java.io.IOException;
import java.util.List;

/**
 * Controller for the Board UI Component
 *
 * @author Isaac D. Griffith
 * @version 1.0.0
 */
public class BoardController {

    private static final String GROWLING_GRAY = "-fx-background-color: #828282; ";
    private static final String ROARANGE = "-fx-background-color: #f47920; ";

    private LOClient app;
    private Node[][] gridPaneArray = null;

    @FXML
    GridPane grid;

    /**
     * Constructor
     *
     * @param app The parent application
     */
    public BoardController(LOClient app) {
        this.app = app;
    }

    /**
     * Handles the events created when pressing the board buttons. The location of the button pressed is passed to the
     * server.
     *
     * @param event The resutling event
     */
    @FXML
    protected void handleBoardButtonAction(ActionEvent event) {
        Object src = event.getSource();
        if (src instanceof Button) {
            Button btn = (Button) src;
            Node node = null;
            for (Node n : grid.getChildren()) {
                if (btn.equals(n)) {
                    node = n;
                    break;
                }
            }
            int x = GridPane.getColumnIndex(node);
            int y = GridPane.getRowIndex(node);
            List<Integer> list = Lists.newArrayList(x, y);

            try {
                GameState state = Connection.instance().sendUpdate(list);
                updateGameBoard(state);
            } catch (IOException | InterruptedException ex) {
                app.showExceptionDialog("Could not update the board", ex);
            }
        }
    }

    /**
     * Processes the event that occurs when the Exit button is pressed
     *
     * @param event The resulting Event
     */
    @FXML
    protected void handleExitButtonAction(ActionEvent event) {
        app.exit();
    }

    /**
     * Processes the event that occurs when the Disconnect button is pressed
     *
     * @param event The resulting Event
     */
    @FXML
    protected void handleDisconnectButtonAction(ActionEvent event) {
        app.disconnect();
    }

    /**
     * Processes the event that occurs when the Reset button is pressed
     *
     * @param event The resulting Event
     */
    @FXML
    protected void handleResetButtonAction(ActionEvent event) {
        reset();
    }

    /**
     * Resets the board display by calling the server reset api command, and updating the board
     */
    protected void reset() {
        try {
            GameState state = Connection.instance().resetGame();
            updateGameBoard(state);
        } catch (IOException | InterruptedException ex) {
            app.showExceptionDialog("Could not reset the board.", ex);
        }
    }

    /**
     * Updates the displayed gameboard with the correspondign provided state
     *
     * @param state The new state of the game
     */
    private void updateGameBoard(GameState state) {
        if (gridPaneArray == null) {
            initGameBoard(state);
        }

        for (int y = 0; y < grid.getRowCount(); y++) {
            for (int x = 0; x < grid.getColumnCount(); x++) {
                if (state.get(x, y) == 1) {
                    ((Button) gridPaneArray[y][x]).setStyle(ROARANGE);
                } else {
                    ((Button) gridPaneArray[y][x]).setStyle(GROWLING_GRAY);
                }
            }
        }

        if (state.isWinner()) {
            app.showWinnerDialog();
        }
    }

    /**
     * Initializes the GameBoard to the provided GameState
     *
     * @param state Current state of the game
     */
    private void initGameBoard(GameState state) {
        int[][] board = state.getBoard();
        gridPaneArray = new Node[board.length][board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[i].length; j++) {
                Button btn = new Button();
                btn.setOnAction(this::handleBoardButtonAction);
                btn.setMaxHeight(Double.MAX_VALUE);
                btn.setMaxWidth(Double.MAX_VALUE);
                grid.add(btn, i, j);
                btn.setStyle(GROWLING_GRAY);
                gridPaneArray[i][j] = btn;
            }
        }
    }

    /**
     * Called to initialize the board
     */
    protected void initState() {
        try {
            GameState state = Connection.instance().getCurrentState();
            updateGameBoard(state);
        } catch (IOException | InterruptedException ex) {
            app.showExceptionDialog("Could not initialize the board", ex);
        }
    }
}
