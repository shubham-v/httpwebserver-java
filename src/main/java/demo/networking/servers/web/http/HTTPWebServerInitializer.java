package demo.networking.servers.web.http;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import demo.networking.servers.web.http.handlers.HomeHandler;
import demo.networking.servers.web.http.handlers.PingHandler;
import demo.networking.servers.web.http.handlers.PingStreamHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

@Slf4j
public class HTTPWebServerInitializer {

    public static void main(String[] args) {
        initializeServer(null, 3);
    }

    private static void initializeServer(HttpServer httpServer, int retryCount) {
        try {
            if (httpServer == null) {
                System.out.println(String.format("Creating server on port: %s", 8080));
                httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
                System.out.println(String.format("Server thread pool size: %s", 250));
                httpServer.setExecutor(Executors.newFixedThreadPool(250));
                System.out.println("Creating contexts");
                createContexts(httpServer);
            }
            System.out.println("Starting server to listen on port 8080");
            httpServer.start();
            System.out.println("Server listening on port 8080");
            retryCount = 3;
        } catch (final Exception exception) {
            if (retryCount > 0) {
                initializeServer(httpServer, --retryCount);
            }
        }
    }

    private static void createContexts(final HttpServer httpServer) {
        HttpContext httpContext = httpServer.createContext("/", new HomeHandler());
        System.out.println(String.format("Created context for path: %s", httpContext.getPath()));
        httpContext = httpServer.createContext("/ping", new PingHandler());
        System.out.println(String.format("Created context for path: %s", httpContext.getPath()));
        httpContext = httpServer.createContext("/stream/ping", new PingStreamHandler());
        System.out.println(String.format("Created context for path: %s", httpContext.getPath()));
    }

}
