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

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.io.IOException;

/**
 * Controller for the Connection Form
 *
 * @author Isaac D. Griffith
 * @version 1.0.0
 */
public class ConnectFormController {

    private LOClient app;

    @FXML
    TextField uri;
    @FXML
    Spinner<Integer> port;

    /**
     * Constructor
     *
     * @param app The parent application
     */
    public ConnectFormController(LOClient app) {
        this.app = app;
    }

    /**
     * Processes the event that occurs when the connect buttion is pressed
     *
     * @param event The resulting event
     */
    @FXML
    protected void handleConnectButtonAction(ActionEvent event) {
        if (uri.getText().isBlank()) {
            app.showWarningDialog("Information Missing", "Server address cannot be blank");
            return;
        }

        Connection connection = Connection.instance();
        connection.initialize(uri.getText(), port.getValue().toString());

        if (connection.initialized && connection.test()) {
            try {
                app.showBoard();
            } catch (IOException ex) {
                app.showExceptionDialog("Couldn't load game board", ex);
            }
        } else {
            app.showWarningDialog("Error Connecting", "Couldn't connect to the server. Verify that you have the correct address and port.");
        }
    }

    /**
     * Processes the event when the reset button is pressed
     *
     * @param event The resulting event
     */
    @FXML
    protected void handleResetButtonAction(ActionEvent event) {
        uri.setText("");
        port.getValueFactory().setValue(0);
    }
}
