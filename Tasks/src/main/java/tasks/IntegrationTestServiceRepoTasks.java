package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class IntegrationTestServiceRepoTasks {

    private static ClassLoader classLoader = IntegrationTestServiceRepoTasks.class.getClassLoader();
    private ArrayTaskList repo;
    private TasksService service;

    @BeforeEach
    void setUp() throws IOException {
        // Fisierul contine un task: "test1", 24 apirlie 2020, activ, nerepetivit
        this.repo = new ArrayTaskList();
        File savedTasksFile = new File(Objects.requireNonNull(classLoader.getResource("data/tasks.txt")).getFile());
        TaskIO.readBinary(this.repo, savedTasksFile);
        this.service = new TasksService(repo);
    }

    @Test
    void getObservableList() {
        assertEquals(1, service.getObservableList().size());
    }

    @Test
    void filterTasksOneTaskInInterval() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2020);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 11);
        Date startDate = cal.getTime();

        cal.set(Calendar.YEAR, 2020);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 27);
        Date endDate = cal.getTime();

        ArrayList rez = (ArrayList) service.filterTasks(startDate, endDate);
        assertEquals(1, rez.size());
    }

    @Test
    void filterTasksNoTasksInInterval() {
        assertIterableEquals(Collections.emptyList(),
                service.filterTasks(new Date(2020, Calendar.MARCH, 1),
                        new Date(2020, Calendar.MARCH, 13)));
    }

}