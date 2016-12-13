import data.Task;
import data.Tasks;
import server.Request;
import server.Response;

import static java.util.Collections.emptyMap;

public class TodoRestTest {

    public static String shouldReturnEmptyListOfTasks() throws Exception {
        // given
        TodoRest todoRest = new TodoRest();

        // when
        Response response = todoRest.tasks(new Request("GET", "/tasks",
                                                       emptyMap(), ""));

        // then
        Tasks tasks = XML.unmarshal(Tasks.class, response.body).get();
        if (tasks.getTasks() == null) {
            return "OK";
        } else {
            return "FAIL: Not return empty list of tasks. Response: " + response.body;
        }
    }

    public static String shouldAddNewTaskToEmptyList() throws Exception {
        // given
        TodoRest todoRest = new TodoRest();
        Task task = new Task();
        task.setTitle("Buy food");

        // when
        Response addTaskResponse = todoRest.tasks(new Request("POST", "/tasks",
                                                              emptyMap(), XML.marshal(Task.class, task)));
        Task newTask = XML.unmarshal(Task.class, addTaskResponse.body).get();

        Response taskListResponse = todoRest.tasks(new Request("GET", "/tasks",
                                                               emptyMap(), ""));

        // then
        Tasks tasks = XML.unmarshal(Tasks.class, taskListResponse.body).get();
        if (tasks.getTasks().get(0).getId() != newTask.getId()
            && tasks.getTasks().get(0).getTitle().equals(task.getTitle())) {
            return "OK";
        } else {
            return "FAIL: Not add task to list. Response: " + addTaskResponse.body + " " + taskListResponse.body;
        }
    }

    public static String shouldGetTaskById() throws Exception {
        // given
        TodoRest todoRest = new TodoRest();
        Task task = new Task();
        task.setTitle("Buy food");

        Response addTaskResponse = todoRest.tasks(new Request("POST", "/tasks",
                                                              emptyMap(), XML.marshal(Task.class, task)));
        Task newTask = XML.unmarshal(Task.class, addTaskResponse.body).get();

        // when
        Response getTaskResponse = todoRest.task(new Request("GET", "/tasks/" + newTask.getId(),
                                                             emptyMap(), ""));

        // then
        Task readTask = XML.unmarshal(Task.class, getTaskResponse.body).get();
        if (readTask.getId().equals(newTask.getId()) &&
            readTask.getTitle().equals(newTask.getTitle())) {
            return "OK";
        } else {
            return "FAIL: Not get task from store. Response: " + getTaskResponse.body;
        }
    }

    public static String shouldReturn404ForNotExistingTask() throws Exception {
        // given
        TodoRest todoRest = new TodoRest();

        // when
        Response getTaskResponse = todoRest.task(new Request("GET", "/tasks/unknown",
                                                             emptyMap(), ""));

        // then
        if (getTaskResponse.code == 404) {
            return "OK";
        } else {
            return "FAIL: Not get task from store. Response: " + getTaskResponse.body;
        }
    }

    public static String shouldUpdateTaskById() throws Exception {
        // given
        TodoRest todoRest = new TodoRest();
        Task task = new Task();
        task.setTitle("Buy food");

        Response addTaskResponse = todoRest.tasks(new Request("POST", "/tasks",
                                                              emptyMap(), XML.marshal(Task.class, task)));
        Task newTask = XML.unmarshal(Task.class, addTaskResponse.body).get();

        // when
        newTask.setChecked(true);

        Response getTaskResponse = todoRest.task(new Request("PUT", "/tasks/" + newTask.getId(),
                                                             emptyMap(), XML.marshal(Task.class, newTask)));

        Response updateTaskResponse = todoRest.task(new Request("GET", "/tasks/" + newTask.getId(),
                                                                emptyMap(), ""));

        // then
        Task readTask = XML.unmarshal(Task.class, getTaskResponse.body).get();
        if (readTask.isChecked()) {
            return "OK";
        } else {
            return "FAIL: Not update task from store. Response: " + getTaskResponse.body;
        }
    }

    public static String shouldDeleteTask() throws Exception {
        // given
        TodoRest todoRest = new TodoRest();
        Task task = new Task();
        task.setTitle("Buy food");

        Response addTaskResponse = todoRest.tasks(new Request("POST", "/tasks",
                                                              emptyMap(), XML.marshal(Task.class, task)));
        Task newTask = XML.unmarshal(Task.class, addTaskResponse.body).get();

        // when
        Response deleteTaskResponse = todoRest.task(new Request("DELETE", "/tasks/" + newTask.getId(),
                                                                emptyMap(), ""));

        Response taskListResponse = todoRest.tasks(new Request("GET", "/tasks",
                                                               emptyMap(), ""));

        // then
        Tasks tasks = XML.unmarshal(Tasks.class, taskListResponse.body).get();
        if (tasks.getTasks() == null) {
            return "OK";
        } else {
            return "FAIL: Not delete task. Response: " + addTaskResponse.body + " " + taskListResponse.body;
        }
    }

}
