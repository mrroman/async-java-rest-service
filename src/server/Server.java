package server;

import java.io.Closeable;
import java.io.IOException;

import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.net.InetSocketAddress;
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
        this.handlers.put("/health", this::healthEndpoint);
        this.handlers.putAll(customHandlers);
    }

    public void start() throws IOException {
        logger.info(() -> "Starting server at port " + port);
        serverChannel = AsynchronousServerSocketChannel.open().bind(new InetSocketAddress(port));

        serverChannel.accept(this, new CompletionHandler<AsynchronousSocketChannel, Object>() {
                public void completed(AsynchronousSocketChannel clientChannel, Object o) {
                    serverChannel.accept(null, this);

                    Server.this.readRequests(clientChannel);
                }

                public void failed(Throwable exc, Object o) {
                    if (!(exc instanceof AsynchronousCloseException)) {
                        logger.warning(exc::toString);
                    }
                }
            });
    }

    protected void readRequests(AsynchronousSocketChannel clientChannel) {
        final ByteBuffer requestBuffer = ByteBuffer.allocate(4096);
        clientChannel.read(requestBuffer, null, new CompletionHandler<Integer, Object>() {
                public void completed(Integer n, Object o) {
                    requestBuffer.flip();
                    final String rawRequest = new String(requestBuffer.array(), requestBuffer.position(), requestBuffer.remaining());
                    final Request request = Request.fromRawRequest(rawRequest);

                    Response response = notFoundResponse;

                    for (Map.Entry<String, Function<Request, Response>> handler : handlers.entrySet()) {
                        if (request.path.startsWith(handler.getKey())) {
                            response = handler.getValue().apply(request);
                            break;
                        }
                    }

                    clientChannel.write(ByteBuffer.wrap(response.toBytes()), clientChannel, closeOnWrite);
                }

                public void failed(Throwable exc, Object o) {
                    if (!(exc instanceof AsynchronousCloseException)) {
                        logger.warning(exc::toString);
                    }
                }

            });
    }

    public void stop() throws IOException {
        serverChannel.close();
    }

    public void close() throws IOException {
        stop();
    }

    private Response healthEndpoint(Request request) {
        return new Response(200, Collections.emptyMap(), "OK");
    }

    private Response notFoundResponse = new Response(404, Collections.emptyMap(), "Not Found");

    private CompletionHandler<Integer, AsynchronousSocketChannel> closeOnWrite =
        new CompletionHandler<Integer, AsynchronousSocketChannel>() {
            public void completed(Integer n, AsynchronousSocketChannel clientChannel) {
                try {
                    clientChannel.close();
                } catch (IOException ex) {
                    logger.warning(ex::getMessage);
                }
            }

            public void failed(Throwable exc, AsynchronousSocketChannel clientChannel) {
                logger.warning(exc::toString);
                completed(0, clientChannel);
            }
        };
}
