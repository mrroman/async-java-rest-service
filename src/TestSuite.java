import server.ServerTest;

import java.util.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ExecutionException;

public class TestSuite {

    public static void execute(List<Callable<String>> tests) throws Exception {
        final ExecutorService testExecutor = Executors.newSingleThreadExecutor();

        testExecutor
            .invokeAll(tests)
            .forEach((x) -> {
                    System.out.println();
                    try {
                        System.out.println(x.get());
                    } catch (InterruptedException | ExecutionException e) {
                        System.err.println("FAIL: Exception during test execution");
                        e.printStackTrace();
                    }
                });

        testExecutor.shutdown();
    }

    public static void main(String []args) throws Exception {
        execute(Arrays.asList(
                              ServerTest::shouldHaveHealthCheck,
                              ServerTest::shouldReturnNotFoundWhenNoEndpointDeclared,
                              ServerTest::shouldHandleWithCustomEndpont,

                              TodoRestTest::shouldReturnEmptyListOfTasks,
                              TodoRestTest::shouldAddNewTaskToEmptyList,
                              TodoRestTest::shouldGetTaskById,
                              TodoRestTest::shouldReturn404ForNotExistingTask,
                              TodoRestTest::shouldUpdateTaskById,
                              TodoRestTest::shouldDeleteTask,
                              () -> "END."));
    }
}
