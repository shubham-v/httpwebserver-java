package demo.networking.servers.web.http.handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import demo.networking.servers.web.http.vo.HandlerErrorResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

@Slf4j
public abstract class AbstractHttpHandler implements HttpHandler {

    protected void sendHeaders(final HttpExchange exchange, final int responseLength, final int retryCount) {
        try {
            log.info("Sending headers to: {}, with response length: {}", exchange.getRemoteAddress(), responseLength);
            exchange.sendResponseHeaders(200, responseLength);
            log.info("Sent headers to: {}, with response length: {}", exchange.getRemoteAddress(), responseLength);
        } catch (final Exception exception) {
            log.error("Exception while sending response: ", exception);
            if (retryCount > 0) {
                sendHeaders(exchange, responseLength, retryCount - 1);
            }
        }
    }

    protected void writeAndCloseResponseStream(final HttpExchange exchange, OutputStream outputStream,
                                               final String response, final int retryCount) {
        outputStream = writeToOutputStream(exchange, null, response, retryCount);
        closeOutputStream(exchange, outputStream, retryCount);
    }

    protected OutputStream writeToOutputStream(final HttpExchange exchange, OutputStream outputStream, final String httpResponse, final int retryCount) {
        String response = Optional.ofNullable(httpResponse).orElse("");
        try {
            if (outputStream == null) {
                outputStream = exchange.getResponseBody();
            }
            log.info("Writing to outputStream and streamimg to: {}, with response: {}", exchange.getRemoteAddress(), response);
            outputStream.write(response.getBytes());
            log.info("Writen to outputStream and streamimg to: {}, with response: {}", exchange.getRemoteAddress(), response);
        } catch (final Exception exception) {
            if (retryCount > 0) {
                writeToOutputStream(exchange, outputStream, response, retryCount - 1);
            }
        }
        return outputStream;
    }

    protected OutputStream writeToOutputStreamAndFlush(final HttpExchange exchange, OutputStream outputStream, final String httpResponse, final int retryCount) {
        String response = Optional.ofNullable(httpResponse).orElse("");
        try {
            if (outputStream == null) {
                outputStream = exchange.getResponseBody();
            }
            log.info("Writing to outputStream and streamimg to: {}, with response: {}", exchange.getRemoteAddress(), response);
            outputStream.write(response.getBytes());
            outputStream.flush();
            log.info("Writen to outputStream and streamimg to: {}, with response: {}", exchange.getRemoteAddress(), response);
        } catch (final Exception exception) {
            if (retryCount > 0) {
                writeToOutputStream(exchange, outputStream, response, retryCount - 1);
            }
        }
        return outputStream;
    }

    protected void closeOutputStream(final HttpExchange exchange, final OutputStream outputStream, final int retryCount) {
        try {
            log.info("Closing response output stream to: {}, with response length: {}", exchange.getRemoteAddress());
            outputStream.close();
            log.info("Closed response output stream to: {}, with response length: {}", exchange.getRemoteAddress());
        } catch (final Exception exception) {
            if (retryCount > 0) {
                closeOutputStream(exchange, outputStream, retryCount - 1);
            }
        }
    }

    protected void end(final HttpExchange exchange, final String response) {
        sendHeaders(exchange, Optional.ofNullable(response).map(String::length).orElse(0), 3);
        writeAndCloseResponseStream(exchange, null, response, 3);
    }

    protected void handleException(final HttpExchange exchange, final Throwable throwable) {
        String response = HandlerErrorResponse.builder()
                .errorType(throwable.getClass().getName())
                .errorMessage(throwable.getMessage())
                .timestamp(new Date())
                .build()
                .toString();
        end(exchange, response);
    }

    public Map<String, String> queryToMap(final String query) {
        Map<String, String> result = new LinkedHashMap<>();
        if (query != null) {
            for (String param : query.split("&")) {
                String[] entry = param.split("=");
                if (entry.length > 1) {
                    result.put(entry[0], entry[1]);
                } else {
                    result.put(entry[0], "");
                }
            }
        }
        return result;
    }

    @Override
    public void handle(final HttpExchange exchange) throws IOException {
        Map<String, String> queryParams = queryToMap(exchange.getRequestURI().getQuery());
        handle(exchange, queryParams);
    }

    protected abstract void handle(final HttpExchange exchange, final Map<String, String> queryParams);


}
