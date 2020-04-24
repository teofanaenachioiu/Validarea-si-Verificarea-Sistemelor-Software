package tasks;

import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.never;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class IntegrationTestServiceRepo {

    private ArrayTaskList repo;
    private TasksService service;

    @BeforeEach
    void setUp() {
        this.repo = new ArrayTaskList();
        this.service = new TasksService(repo);
    }

    @Test
    void getObservableList() {
        assertIterableEquals(FXCollections.observableArrayList(Collections.emptyList()), service.getObservableList());

        Task t1 = new Task("task1", new Date());
        repo.add(t1);
        assertIterableEquals(FXCollections.observableArrayList(Collections.singletonList(t1)), service.getObservableList());
    }

    @Test
    void filterTasksNoTasksInList() {
        assertIterableEquals(Collections.emptyList(), service.filterTasks(new Date(2020,4,1), new Date(2020,4,3)));
    }

    @Test
    void filterTasksOneTaskInList() {
        Task task1 = new Task("task1", new Date(2020, 3, 11));
        task1.setActive(true);
        Task task2 = new Task("task2", new Date(2020, 3, 11), new Date(2020, 3, 17), 3600);
        repo.add(task1);
        repo.add(task2);

        // verific nr de apeluri ale unei metode din mock, metoda apelata din service
        assertIterableEquals(Collections.singletonList(task1), service.filterTasks(new Date(2020,3,1), new Date(2020,3,13)));
    }

    @Test
    void filterTasksTasksInListNoActive() {
        Task task1 = new Task("task1", new Date(2020, 3, 11));
        Task task2 = new Task("task2", new Date(2020, 3, 11), new Date(2020, 3, 17), 3600);
        repo.add(task1);
        repo.add(task2);

        assertIterableEquals(Collections.emptyList(), service.filterTasks(new Date(2020,3,1), new Date(2020,3,13)));
    }

}