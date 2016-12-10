import java.io.FileNotFoundException;

import java.util.*;

import static java.util.Collections.emptyMap;
import static java.util.Collections.singletonMap;

public class ServerTest {

    public final static Map<String, String> HEADERS = emptyMap();

    public static String shouldHaveHealthCheck() throws Exception {
        try (Server server = new Server(8080, emptyMap())) {
            server.start();

            String response = Client.sendRequest(new Request("GET",
                                                             "http://localhost:8080/health",
                                                             HEADERS,
                                                             null));

            if (response.equals("OK")) {
                return "OK";
            } else {
                return "FAIL: No health check. Response: " + response;
            }
        }
    }

    public static String shouldReturnNotFoundWhenNoEndpointDeclared() throws Exception {
        try (Server server = new Server(8080, emptyMap())) {
            server.start();

            String response = Client.sendRequest(new Request("GET",
                                                             "http://localhost:8080/unknown",
                                                             HEADERS,
                                                             null));

            return "FAIL: No 404 response";
        } catch (FileNotFoundException ex) {
            return "OK";
        }
    }

    public static String shouldHandleWithCustomEndpont() throws Exception {
        try (Server server = new Server(8080,
                                        singletonMap("/custom", (r) -> new Response(200,
                                                                                    Collections.emptyMap(),
                                                                                    "SUPER")))) {
            server.start();

            String response = Client.sendRequest(new Request("GET",
                                                             "http://localhost:8080/custom",
                                                             HEADERS,
                                                             null));

            if (response.equals("SUPER")) {
                return "OK";
            } else {
                return "FAIL: No health check. Response: " + response;
            }
        }
    }

}
