package cs.isu.edu.cs3321.server;

import com.google.gson.Gson;
import cs.isu.edu.cs3321.lightsout.GameState;
import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class FunctionalTests {

    private static LOServer server = new LOServer();

    @BeforeAll
    private static void setUp() {
        server.getApp().start(7000);
    }

    @AfterAll
    private static void tearDown() {
        server.getApp().stop();
    }

    @Test
    public void GET_to_fetch_current_game_state() {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:7000/api/state").asJson();
        String data = response.getBody().toString();
        GameState state = new Gson().fromJson(data, GameState.class);
        assertThat(state).isNotNull();
        assertThat(state.isWinner()).isFalse();
    }

    @Test
    public void POST_to_update_location() {
        List<Integer> update = Lists.newArrayList(1, 1);
        HttpResponse<JsonNode> response = Unirest.post("http://localhost:7000/api/update")
                .contentType("application/json")
                .body(new Gson().toJson(update))
                .asJson();
        GameState state = new Gson().fromJson(response.getBody().toString(), GameState.class);
        assertThat(state).isNotNull();
    }

    @Test
    public void GET_to_reset_game() {
        HttpResponse<JsonNode> response = Unirest.get("http://localhost:7000/api/reset").asJson();
        String data = response.getBody().toString();
        GameState state = new Gson().fromJson(data, GameState.class);
        assertThat(state).isNotNull();
        assertThat(state.isWinner()).isFalse();
    }

    @Test
    public void GET_to_determine_server_is_running() {
        HttpResponse<String> response = Unirest.get("http://localhost:7000/api/status").asString();
        assertThat(response.getBody()).isEqualTo("OK");
    }
}
