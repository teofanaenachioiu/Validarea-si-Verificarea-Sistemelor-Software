package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class IntegrationTestServiceRepoTasks {

    private TasksService service;

    @BeforeEach
    void setUp() throws IOException {
        ArrayTaskList repo = new ArrayTaskList();

        String resourceName = "tasks.txt";
        File savedTasksFile = new File(Objects.requireNonNull(getClass().getClassLoader().getResource(resourceName)).getFile());
        TaskIO io = new TaskIO();
        io.readText(repo, savedTasksFile);
        this.service = new TasksService(repo);
    }


    @Test
    void getObservableList() {
        assertEquals(1, service.getObservableList().size());
        System.out.println(service.getObservableList().get(0));
    }

    @Test
    void filterTasksOneTaskInInterval() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2021);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = cal.getTime();

        cal.set(Calendar.YEAR, 2021);
        cal.set(Calendar.MONTH, Calendar.APRIL);
        cal.set(Calendar.DAY_OF_MONTH, 27);
        Date endDate = cal.getTime();

        ArrayList rez = (ArrayList) service.filterTasks(startDate, endDate);
        assertEquals(1, rez.size());
    }

    @Test
    void filterTasksNoTasksInInterval() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2020);
        cal.set(Calendar.MONTH, Calendar.MARCH);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = cal.getTime();

        cal.set(Calendar.YEAR, 2020);
        cal.set(Calendar.MONTH, Calendar.MARCH);
        cal.set(Calendar.DAY_OF_MONTH, 8);
        Date endDate = cal.getTime();

        assertIterableEquals(Collections.emptyList(),
                service.filterTasks(startDate, endDate));
    }

}