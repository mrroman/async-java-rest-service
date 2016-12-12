import data.Task;
import data.Tasks;

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
        switch (request.method.toLowerCase()) {
        case "get": {
            Tasks tasks = new Tasks();
            tasks.setTasks(new ArrayList<>(tasksMap.values()));

            return new Response(200, emptyMap(), XML.marshal(Tasks.class, tasks));
        }
        case "post": {
            Optional<Task> task = XML.unmarshal(Task.class, request.body);

            return task.map((t) -> {
                    String uuid = UUID.randomUUID().toString();
                    t.setId(uuid);

                    tasksMap.put(uuid, t);
                    return new Response(200, emptyMap(), XML.marshal(Task.class, t));
                })
                .orElse(new Response(400, emptyMap(), "Bad request"));
        }
        default:
            return new Response(404, emptyMap(), "Unknown operation");
        }
    }

    public Response task(Request request) {
        switch (request.method.toLowerCase()) {
        case "get": {
            String id = request.path.substring("/tasks/".length());
            Task task = tasksMap.get(id);

            if (task != null) {
                return new Response(200, emptyMap(), XML.marshal(Task.class, task));
            } else {
                return new Response(404, emptyMap(), id + " not found");
            }
        }
        case "put": {
            String id = request.path.substring("/tasks/".length());
            Task update = XML.unmarshal(Task.class, request.body).get();
            Task task = tasksMap.get(id);

            task.setTitle(update.getTitle());
            task.setChecked(update.isChecked());

            return new Response(200, emptyMap(), XML.marshal(Task.class, task));
        }
        case "delete": {
            String id = request.path.substring("/tasks/".length());

            tasksMap.remove(id);

            return new Response(200, emptyMap(), "");
        }
        default:
            return new Response(404, emptyMap(), "Unknown operation");
        }
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
