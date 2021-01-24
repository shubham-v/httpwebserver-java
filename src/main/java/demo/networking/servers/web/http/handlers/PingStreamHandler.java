package demo.networking.servers.web.http.handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.OutputStream;
import java.util.Map;

public class PingStreamHandler extends AbstractHttpHandler {

    @Override
    public void handle(final HttpExchange exchange, final Map<String, String> queryParams) {
        try {
            long bufferTimeInMilliSeconds = Long.valueOf(queryParams.get("buffer"));
            long awaitTime = (long) Math.ceil(bufferTimeInMilliSeconds / 9);
            sendHeaders(exchange, 0, 3);
            Thread.sleep(awaitTime);
            OutputStream outputStream = writeToOutputStreamAndFlush(exchange, null, "Haan mil gai\n", 3);
            Thread.sleep(awaitTime);
            writeToOutputStreamAndFlush(exchange, outputStream, "Dekh ke btata hai\n", 3);
            Thread.sleep(awaitTime);
            writeToOutputStreamAndFlush(exchange, outputStream, "nahi abhi nahi dekha\n", 3);
            Thread.sleep(awaitTime);
            writeToOutputStreamAndFlush(exchange, outputStream, "thori der main btata hai\n", 3);
            Thread.sleep(awaitTime);
            writeToOutputStreamAndFlush(exchange, outputStream, "haan dekh liya\n", 3);
            Thread.sleep(awaitTime);
            writeToOutputStreamAndFlush(exchange, outputStream, "ruko response bhejta hai\n", 3);
            Thread.sleep(awaitTime);
            writeToOutputStreamAndFlush(exchange, outputStream, "response mila\n", 3);
            Thread.sleep(awaitTime);
            writeToOutputStreamAndFlush(exchange, outputStream, "ab mil gaya hoga\n", 3);
            Thread.sleep(awaitTime);
            writeToOutputStreamAndFlush(exchange, outputStream, "chalo thik hai\n", 3);
            Thread.sleep(awaitTime);
            writeToOutputStreamAndFlush(exchange, outputStream, "thankuo\n", 3);
            closeOutputStream(exchange, outputStream, 3);
        } catch (Exception exception) {
            handleException(exchange, exception);
        }
    }

}
