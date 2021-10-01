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
package cs.isu.edu.cs3321.server;

import cs.isu.edu.cs3321.lightsout.Game;
import io.javalin.Javalin;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

import java.util.List;
import java.util.Objects;

import static io.javalin.apibuilder.ApiBuilder.get;
import static io.javalin.apibuilder.ApiBuilder.post;

/**
 * The Lights Out Microservice
 *
 * @author Isaac D. Griffith
 * @version 1.0.0
 */
public class LOServer {

    /**
     * Entry point from the command line
     *
     * @param args Command line arguments (currently nothing is supported)
     */
    public static void main(String[] args) {
        Game game = new Game();
        QueuedThreadPool queuedThreadPool = new QueuedThreadPool(200, 8, 60000);

        Javalin app = Javalin.create(config ->
                config.server(() ->
                        new Server(queuedThreadPool))).start(7000);

        app.routes(() -> {
            get("/api/state", ctx -> ctx.json(game.getState()));
            post("/api/update", ctx -> {
                if (Objects.equals(ctx.contentType(), "application/json")) {
                    List<Integer> list = ctx.bodyAsClass(List.class);
                    game.update(list.get(0), list.get(1));
                    ctx.json(game.getState());
                }
            });
            get("/api/reset", ctx -> {
                game.reset();
                ctx.json(game.getState());
            });
            get("/api/status", ctx -> {
                ctx.result("OK");
            });
        });
    }
}
