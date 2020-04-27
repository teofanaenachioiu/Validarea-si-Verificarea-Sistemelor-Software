package java.tasks;

import javafx.collections.FXCollections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import tasks.ArrayTaskList;
import tasks.Task;
import tasks.TasksService;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.never;
import static org.mockito.internal.verification.VerificationModeFactory.times;

class TasksServiceTestWithMock {

    @Mock
    private ArrayTaskList repo;

    @InjectMocks
    private TasksService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getObservableList() {
        Task t1 = new Task("task1", new Date());
        Task t2 = new Task("task2", new Date());

        Mockito.when(repo.getAll()).thenReturn(Arrays.asList(t1, t2));

        // verific nr de apeluri ale unei metode din mock, metoda apelata din service
        Mockito.verify(repo, never()).getAll();

        assertIterableEquals(service.getObservableList(),
                FXCollections.observableArrayList(Arrays.asList(t1, t2)));

        Mockito.verify(repo, times(1)).getAll();
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
        Mockito.when(repo.getAll()).thenReturn(Arrays.asList(task1, task2));

        // verific nr de apeluri ale unei metode din mock, metoda apelata din service
        Mockito.verify(repo, never()).getAll();
        assertIterableEquals(Collections.singletonList(task1), service.filterTasks(new Date(2020,3,1), new Date(2020,3,13)));
        Mockito.verify(repo, times(1)).getAll();
    }

    @Test
    void filterTasksTasksInListNoActive() {
        Task task1 = new Task("task1", new Date(2020, 3, 11));
        Task task2 = new Task("task2", new Date(2020, 3, 11), new Date(2020, 3, 17), 3600);
        Mockito.when(repo.getAll()).thenReturn(Arrays.asList(task1, task2));

        // verific nr de apeluri ale unei metode din mock, metoda apelata din service
        Mockito.verify(repo, never()).getAll();
        assertIterableEquals(Collections.emptyList(), service.filterTasks(new Date(2020,3,1), new Date(2020,3,13)));
        Mockito.verify(repo, times(1)).getAll();
    }

    @Test
    void getIntervalInHoursNoInterval() {
        Task task = new Task("task", new Date());
        assertEquals("00:00", service.getIntervalInHours(task));
    }

    @Test
    void getIntervalInHoursWithInterval() {
        Task task = new Task("task", new Date(2020, 3, 11), new Date(2020, 3, 12), 3600);
        assertEquals("01:00", service.getIntervalInHours(task));
    }

    @Test
    void formTimeUnit11() {
        assertEquals("11", service.formTimeUnit(11));
    }

    @Test
    void formTimeUnit10() {
        assertEquals("10", service.formTimeUnit(10));
    }

    @Test
    void formTimeUnit9() {
        assertEquals("09", service.formTimeUnit(9));
    }

    @Test
    void formTimeUnit0() {
        assertEquals("00", service.formTimeUnit(0));
    }

    @Test
    void parseFromStringToSeconds() {
        assertEquals(5400, service.parseFromStringToSeconds("1:30"));
    }
}