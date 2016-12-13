import data.Task;
import server.Request;
import server.Response;
import server.Server;

import java.util.*;
import java.util.function.Function;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Collections.emptyMap;

public class TodoRest {

    private Map<String, Task> tasksMap = new ConcurrentHashMap<>();

    public Response tasks(Request request) {
        return new Response(404, emptyMap(), "Not found");
    }

    public Response task(Request request) {
        return new Response(404, emptyMap(), "Not found");
    }

    public static void main(String[] args) throws Exception {
        TodoRest todoRest = new TodoRest();

        Map<String, Function<Request, Response>> endpoints = new LinkedHashMap<>();
        endpoints.put("/tasks/", todoRest::task);
        endpoints.put("/tasks", todoRest::tasks);

        Server server = new Server(8080, endpoints);
        server.start();

        Thread.sleep(Long.MAX_VALUE);
    }

}
