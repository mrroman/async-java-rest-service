package server;

import java.io.OutputStream;
import java.io.IOException;
import java.net.*;
import java.util.*;

public class Client {

    public static String sendRequest(Request request) throws IOException {
        HttpURLConnection conn = (HttpURLConnection)new URL(request.path).openConnection();
        conn.setConnectTimeout(1000);
        conn.setReadTimeout(1000);
        conn.setRequestMethod(request.method);
        for (final String name : request.headers.keySet()) {
            conn.setRequestProperty(name, request.headers.get(name));
        }

        if (!request.method.equalsIgnoreCase("get") && !request.method.equalsIgnoreCase("delete")) {
            conn.setDoOutput(true);
            try (OutputStream output = conn.getOutputStream()) {
                output.write(request.body.getBytes());
            }
        }

        try (Scanner scanner = new Scanner(conn.getInputStream())) {
            return scanner.useDelimiter("\\A").next();
        }
    }

}
