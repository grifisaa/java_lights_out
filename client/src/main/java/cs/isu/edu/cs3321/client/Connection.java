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

import com.google.gson.Gson;
import cs.isu.edu.cs3321.lightsout.GameState;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.List;

/**
 * Singleton Helper class for connecting to the microservice backend
 *
 * @author Isaac D. Griffith
 * @version 1.0.0
 */
public class Connection {

    private static final String RESET_CALL = "http://%s:%s/api/reset";
    private static final String STATE_CALL = "http://%s:%s/api/state";
    private static final String UPDATE_CALL = "http://%s:%s/api/update";
    private static final String STATUS_CALL = "http://%s:%s/api/status";

    String address;
    String port;
    boolean initialized = false;
    HttpClient client;

    /**
     * Private default constructor
     */
    private Connection() {
    }

    /**
     * Singleton helper class
     */
    private static class ConnectionHelper {
        private static final Connection INSTANCE = new Connection();
    }

    /**
     * Singleton instance method
     *
     * @return The single instance of this class
     */
    public static Connection instance() {
        return ConnectionHelper.INSTANCE;
    }

    /**
     * Initializes the singleton with the proper address and port to connect to the microservice backend
     *
     * @param address Address of the service
     * @param port    Port for the service
     */
    public void initialize(String address, String port) {
        this.address = address;
        this.port = port;
        initialized = true;

        client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .connectTimeout(Duration.ofSeconds(20))
                .build();
    }

    /**
     * Disconnects by setting the address, port, and client to null
     */
    public void disconnect() {
        address = null;
        port = null;
        client = null;
    }

    /**
     * Sends an update to the service using the list of params.
     *
     * @param list List containing the x,y coordinate that was clicked
     * @return An updated game state from the service
     * @throws IOException          if there was an error connecting to the service via the network
     * @throws InterruptedException if the update timed out
     */
    public GameState sendUpdate(List<Integer> list) throws IOException, InterruptedException {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        HttpRequest request = createPost(UPDATE_CALL, json);
        return getGameState(request);
    }

    /**
     * Retrieves the current game state from the service
     *
     * @return The current game state
     * @throws IOException          if there was an error connecting to the service via the network
     * @throws InterruptedException if the request timed out
     */
    public GameState getCurrentState() throws IOException, InterruptedException {
        HttpRequest request = createGet(STATE_CALL);
        return getGameState(request);
    }

    /**
     * Call the service to reset the game board to a random state
     *
     * @return The newly reset gamestate
     * @throws IOException          if there was an error connecting to the service via the network
     * @throws InterruptedException if the request timed out
     */
    public GameState resetGame() throws IOException, InterruptedException {
        HttpRequest request = createGet(RESET_CALL);
        return getGameState(request);
    }

    /**
     * Constructs a new HttpRequest object using the POST method, for the provided format string of the api call, and the provided json data to be sent
     *
     * @param apiCall Format string for the api call
     * @param json    json data to be sent
     * @return A HttpRequest object ready to be used with the service
     */
    private HttpRequest createPost(String apiCall, String json) {
        return HttpRequest.newBuilder()
                .uri(URI.create(String.format(apiCall, address, port)))
                .timeout(Duration.ofSeconds(30))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();
    }

    /**
     * Constructs a new HttpRequest object using the GET method, for the provided format string of the api call
     *
     * @param apiCall Format string for the api call
     * @return The newly constructed HttpRequest object ready to be used with the service
     */
    private HttpRequest createGet(String apiCall) {
        return HttpRequest.newBuilder()
                .uri(URI.create(String.format(apiCall, address, port)))
                .timeout(Duration.ofSeconds(30))
                .GET()
                .build();
    }

    /**
     * Sends a GET request to obtain the current state of the game using the provided HttpRequest object
     *
     * @param request the request object for the GET call
     * @return The current game state
     * @throws IOException          if there was an error connecting to the service via the network
     * @throws InterruptedException if the request timed out
     */
    private GameState getGameState(HttpRequest request) throws IOException, InterruptedException {
        Gson gson = new Gson();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        GameState state = gson.fromJson(response.body(), GameState.class);
        return state;
    }

    /**
     * Method to test whether the server is up and running and we have the correct address and port to connect to it.
     *
     * @return True if the client can connect to the service, false otherwise
     */
    public boolean test() {
        HttpRequest request = createGet(STATUS_CALL);
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body().equals("OK");
        } catch (IOException | InterruptedException ex) {
            return false;
        }
    }
}
