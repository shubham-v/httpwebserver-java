package demo.networking.servers.web.http.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.util.Map;

public class PingHandler extends AbstractHttpHandler {

    @Override
    public void handle(final HttpExchange exchange, final Map<String, String> queryParams) {
        end(exchange, "PONG");
    }

}
