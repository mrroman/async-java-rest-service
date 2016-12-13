package server;

import java.io.Closeable;
import java.io.IOException;

import java.nio.channels.*;
import java.util.*;
import java.util.function.*;
import java.util.logging.*;

public class Server implements Closeable {

    private Logger logger = Logger.getLogger(Server.class.getName());

    private int port;
    private Map<String, Function<Request, Response>> handlers = new LinkedHashMap<>();
    private AsynchronousServerSocketChannel serverChannel;

    public Server(int port, Map<String, Function<Request, Response>> customHandlers) {
        this.port = port;
        this.handlers.putAll(customHandlers);
    }

    public void start() throws IOException {
    }

    public void stop() throws IOException {
    }

    public void close() throws IOException {
    }

}
