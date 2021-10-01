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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;
import jiconfont.icons.font_awesome.FontAwesome;
import jiconfont.javafx.IconFontFX;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

/**
 * The Lights Out Desktoop Client
 *
 * @author Isaac D. Griffith
 * @version 1.0.0
 */
public class LOClient extends Application {

    public static final String CONNECT_FORM_FXML = "/ConnectForm.fxml";
    public static final String BOARD_FXML = "/Board.fxml";
    public static final String CONNECTFORM_CSS = "/connectform.css";
    public static final String BOARD_CSS = "/board.css";

    Stage mainStage;
    BoardController controller;

    /**
     * The command line entry point which starts the UI
     *
     * @param args Command line arguments (currently nothing is supported)
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage stage) throws Exception {
        IconFontFX.register(FontAwesome.getIconFont());

        this.mainStage = stage;
        stage.setTitle("Lights Out");

        showConnect();

        stage.show();
    }

    /**
     * Displays the connection form
     *
     * @throws IOException if the connection form fxml cannot be loaded
     */
    public void showConnect() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setController(new ConnectFormController(this));
        loader.setLocation(getClass().getResource(CONNECT_FORM_FXML));
        Parent root = loader.load();
        Scene scene = new Scene(root, 450, 130);
        String css = this.getClass().getResource(CONNECTFORM_CSS).toExternalForm();
        scene.getStylesheets().add(css);
        mainStage.setScene(scene);
    }

    /**
     * Displays the Lights Out Board component
     *
     * @throws IOException if the lights out board component fxml cannot be loaded
     */
    public void showBoard() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        controller = new BoardController(this);
        loader.setController(controller);
        loader.setLocation(getClass().getResource(BOARD_FXML));
        Parent root = loader.load();
        Scene scene = new Scene(root, 500, 500);
        String css = this.getClass().getResource(BOARD_CSS).toExternalForm();
        scene.getStylesheets().add(css);
        mainStage.setScene(scene);

        controller.initState();
    }

    /**
     * Disconnects from the current server and shows teh connection form again.
     */
    public void disconnect() {
        try {
            Connection.instance().disconnect();
            showConnect();
        } catch (Exception ex) {
            showExceptionDialog("Could not load the FXML for the Connect Form", ex);
        }
    }

    /**
     * Shows the winner dialog
     */
    public void showWinnerDialog() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Congratulations");
        alert.setHeaderText("You Won!!!");
        alert.setContentText("Congratulations on this stunning achievement. Would you like to play again?");

        ButtonType buttonTypeExit = new ButtonType("Exit");
        ButtonType buttonTypeDisconnect = new ButtonType("Disconnect");
        ButtonType buttonTypeNewGame = new ButtonType("New Game");

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeExit) {
            exit();
        } else if (result.get() == buttonTypeDisconnect) {
            disconnect();
        } else if (result.get() == buttonTypeNewGame) {
            controller.reset();
        }
    }

    /**
     * Shows an exception dialog with an expandable component including the stack trace.
     *
     * @param content Brief message about why the exception occurred.
     * @param ex      The actual exception that occurred.
     */
    public void showExceptionDialog(String content, Exception ex) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Oops, an error occurred.");
        alert.setContentText(content);

        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        ex.printStackTrace(pw);
        String exceptionText = sw.toString();

        Label label = new Label("The exception stacktrace was:");

        TextArea text = new TextArea(exceptionText);
        text.setEditable(false);
        text.setWrapText(true);
        text.setMaxWidth(Double.MAX_VALUE);
        text.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(text, Priority.ALWAYS);
        GridPane.setHgrow(text, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(text, 0, 1);

        alert.getDialogPane().setExpandableContent(expContent);

        alert.showAndWait();
    }

    /**
     * Displays a warning message with the given title and a brief message describing the warning and how to correct it.
     *
     * @param title   Title for the warning
     * @param message The message
     */
    public void showWarningDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);

        alert.showAndWait();
    }

    /**
     * Exits the application
     */
    public void exit() {
        disconnect();
        mainStage.close();
        System.exit(0);
    }
}
