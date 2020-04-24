package tasks;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;

class ArrayTaskListTestWithSpy {

    private ArrayTaskList repo;

    @BeforeEach
    void setUp() {
        this.repo = new ArrayTaskList();
    }

    @Test
    void add() {
        ArrayTaskList spiedRepo = spy(repo);
        Task t1 = new Task("t1", new Date());
        assertIterableEquals(Collections.emptyList(), spiedRepo.getAll());
        spiedRepo.add(t1);
        assertIterableEquals(Collections.singletonList(t1), spiedRepo.getAll());

        assertIterableEquals(Collections.emptyList(), repo.getAll());
    }

    @Test
    void removeNoTask() {
        ArrayTaskList spiedRepo = spy(repo);
        Task t1 = new Task("t1", new Date());
        Task t2 = new Task("t2", new Date());
        spiedRepo.add(t1);

        assertFalse(spiedRepo.remove(t2));
        assertIterableEquals(Collections.singletonList(t1), spiedRepo.getAll());

        assertEquals(0, repo.size());
    }

    @Test
    void removeTask() {
        ArrayTaskList spiedRepo = spy(repo);
        Task t1 = new Task("t1", new Date());
        spiedRepo.add(t1);
        assertIterableEquals(Collections.singletonList(t1), spiedRepo.getAll());

        assertTrue(spiedRepo.remove(t1));
        assertIterableEquals(Collections.emptyList(), spiedRepo.getAll());
        assertEquals(0, repo.size());
    }

    @Test
    void size() {
        ArrayTaskList spiedRepo = spy(repo);
        assertEquals(0, spiedRepo.size());

        Mockito.when(spiedRepo.size()).thenReturn(1);
        assertEquals(1, spiedRepo.size());

        assertEquals(0, repo.size());
    }

    @Test
    void getTaskInvalidIndex() {
        ArrayTaskList spiedRepo = spy(repo);
        assertThrows(IndexOutOfBoundsException.class, ()->spiedRepo.getTask(1));
    }

    @Test
    void getTaskValidIndex() {
        ArrayTaskList spiedRepo = spy(repo);

        Task t1 = new Task("t1", new Date());
        Task t2 = new Task("t2", new Date());
        spiedRepo.add(t1);
        spiedRepo.add(t2);
        assertEquals(t1, spiedRepo.getTask(0));
    }

    @Test
    void getAll() {
        ArrayTaskList spiedRepo = spy(repo);
        // se valideaza functia getAll cu adevarata ei valoare returnata
        assertIterableEquals(Collections.emptyList(), spiedRepo.getAll());

        Task t1 = new Task("t1", new Date());
        spiedRepo.add(t1);

        // se valideaza functia getAll cu valoarea ei returnata de spy
        assertIterableEquals(Collections.singletonList(t1), spiedRepo.getAll());

        // se valideaza functia getAll data de repo
        assertIterableEquals(Collections.emptyList(), repo.getAll());
    }
}