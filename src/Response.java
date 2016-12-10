import java.util.*;

public class Response {

    public final int code;
    public final Map<String, String> headers;
    public final String body;

    public Response(int code,
                    Map<String, String> headers,
                    String body) {
        this.code = code;
        this.headers = Collections.unmodifiableMap(new HashMap<>(headers));
        this.body = body;
    }

    public byte[] toBytes() {
        StringBuilder sb = new StringBuilder();
        sb.append("HTTP/1.1 ");
        sb.append(code);
        sb.append("\r\n");

        for (Map.Entry<String, String> header : headers.entrySet()) {
            sb.append(header.getKey());
            sb.append(": ");
            sb.append(header.getValue());
            sb.append("\r\n");
        }
        sb.append("\r\n");
        sb.append(body);

        return sb.toString().getBytes();
    }
}
