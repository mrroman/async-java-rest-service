package server;

import java.util.*;

public class Request {

    public final String method;
    public final String path;
    public final Map<String, String> headers;
    public final String body;

    public Request(final String method,
                   final String path,
                   final Map<String, String> headers,
                   final String body) {
        this.method = method;
        this.path = path;
        this.headers = Collections.unmodifiableMap(new HashMap<>(headers));
        this.body = body;
    }

    public static Request fromRawRequest(final String rawRequest) {
        final int methodLineEnd = rawRequest.indexOf("\r\n");
        final String[] methodLine = rawRequest.substring(0, methodLineEnd).split(" ");

        final String method = methodLine[0];
        final String path = methodLine[1];

        final int headersEnd = rawRequest.indexOf("\r\n\r\n");
        final String headers = rawRequest.substring(methodLineEnd + 2, headersEnd);
        final String body = rawRequest.substring(headersEnd + 4);

        final Map<String, String> headersMap = new HashMap<>();
        for (final String headerLine : headers.split("\r\n")) {
            if (headerLine.length() == 0) {
                break;
            }

            final int collonPos = headerLine.indexOf(":");
            final String key = headerLine.substring(0, collonPos);
            final String value = headerLine.substring(collonPos + 1);
            headersMap.put(key, value);
        }

        return new Request(method, path, headersMap, body);
    }
}
