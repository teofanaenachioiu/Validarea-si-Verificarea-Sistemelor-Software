package tasks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertIterableEquals;


class TasksOperationsTest {
    private Date date1 = new Date(2020, Calendar.MARCH, 11);
    private Date date2 = new Date(2020, Calendar.MARCH, 12);
    private Date date3 = new Date(2020, Calendar.MARCH, 18);
    private Date date4 = new Date(2020, Calendar.MARCH, 21);
    private Date date5 = new Date(2020, Calendar.MARCH, 25);
    private Date date6 = new Date(2020, Calendar.MARCH, 28);
    private ArrayTaskList taskListEmpty = new ArrayTaskList();
    private ObservableList<Task> tasksEmpty = FXCollections.observableArrayList();
    private TasksOperations tasksOperationsEmpty = new TasksOperations(tasksEmpty);
    private ArrayTaskList taskListFill;
    private ObservableList<Task> tasksFill;
    private TasksOperations tasksOperationsFill;
    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;

    @BeforeEach
    void setUp() {
        task1 = new Task("title1", date1); // no repeated and no active
        task1.setActive(false);
        task1.setTime(date1);
        task2 = new Task("title2", date2, date3, 5); // repeated and no active
        task2.setActive(false);
        task3 = new Task("title3", date4, date5, 2); // repeated and active
        task3.setActive(true);
        task4 = new Task("title4", date6); // no repeated and active
        task4.setActive(true);
        task1.setTime(date3);

        this.taskListFill = new ArrayTaskList();
        taskListFill.add(task1);
        taskListFill.add(task2);
        taskListFill.add(task3);
        taskListFill.add(task4);
        this.tasksFill = FXCollections.observableArrayList(taskListFill.getAll());
        this.tasksOperationsFill = new TasksOperations(tasksFill);
    }

    @Test
    void noItemsInTaskListTC01() {
        assertIterableEquals(taskListEmpty, tasksOperationsEmpty.incoming(date1, date2));
    }

    @Test
    void startDataAfterToEndDateTC02() {
        assertIterableEquals(taskListEmpty, tasksOperationsFill.incoming(date2, date1));
    }

    @Test
    void startDataEqualToEndDateTC03() {
        assertIterableEquals(taskListEmpty, tasksOperationsEmpty.incoming(date1, date1));
    }

    @Test
    void noRepeatedNoActiveTaskAndStartNotBeforeTimeTaskTC04() {
        Date start = new Date(2020, Calendar.MARCH, 10);
        Date end = new Date(2020, Calendar.MARCH, 12);
        assertIterableEquals(taskListEmpty, tasksOperationsFill.incoming(start, end));
    }

    @Test
    void repeatedNoActiveTaskAndStartNotBeforeTimeTaskTC05() {
        Date start = new Date(2020, Calendar.MARCH, 13);
        Date end = new Date(2020, Calendar.MARCH, 19);
        assertIterableEquals(taskListEmpty, tasksOperationsFill.incoming(start, end));
    }

    @Test
    void repeatedActiveTaskNextTimeAfterEndTC06() {
        Date start = new Date(2020, Calendar.MARCH, 19);
        Date end = new Date(2020, Calendar.MARCH, 20);
        assertIterableEquals(taskListEmpty, tasksOperationsFill.incoming(start, end));
    }

    @Test
    void repeatedActiveTaskAndNextTimeBeforeEndTC07() {
        Date start = new Date(2020, Calendar.MARCH, 19);
        Date end = new Date(2020, Calendar.MARCH, 22);
        ArrayTaskList taskList = new ArrayTaskList();
        taskList.add(task3);
        assertIterableEquals(taskList, tasksOperationsFill.incoming(start, end));
    }

    @Test
    void repeatedActiveTaskAndNextTimeEqualToEndTC08() {
        Date start = new Date(2020, Calendar.MARCH, 19);
        Date end = new Date(2020, Calendar.MARCH, 21);
        ArrayTaskList taskList = new ArrayTaskList();
        taskList.add(task3);
        assertIterableEquals(taskList, tasksOperationsFill.incoming(start, end));
    }

    @Test
    void noRepeatedActiveTaskAndStartDateBeforeTaskDateAndEndDateEqualToTaskDateTC11() {
        Date start = new Date(2020, Calendar.MARCH, 27);
        Date end = new Date(2020, Calendar.MARCH, 28);
        ArrayTaskList taskList = new ArrayTaskList();
        taskList.add(task4);
        assertIterableEquals(taskList, tasksOperationsFill.incoming(start, end));
    }

    @Test
    void noRepeatedActiveTaskAndStartBeforeTaskDateAndTaskDateBeforeEndTC12() {
        Date start = new Date(2020, Calendar.MARCH, 27);
        Date end = new Date(2020, Calendar.MARCH, 29);
        ArrayTaskList taskList = new ArrayTaskList();
        taskList.add(task4);
        assertIterableEquals(taskList, tasksOperationsFill.incoming(start, end));
    }

    @Test
    void noRepeatedActiveTaskAndStartBeforeTaskDateAndNoTaskDateBeforeEndTC13() {
        Date start = new Date(2020, Calendar.MARCH, 26);
        Date end = new Date(2020, Calendar.MARCH, 27);
        assertIterableEquals(taskListEmpty, tasksOperationsFill.incoming(start, end));
    }

    @Test
    void noRepeatedActiveTaskAndStartBeforeTaskDateAndTaskDateEqualsToEndTC14() {
        Date start = new Date(2020, Calendar.MARCH, 26);
        Date end = new Date(2020, Calendar.MARCH, 28);
        ArrayTaskList taskList = new ArrayTaskList();
        taskList.add(task4);
        assertIterableEquals(taskList, tasksOperationsFill.incoming(start, end));
    }
}