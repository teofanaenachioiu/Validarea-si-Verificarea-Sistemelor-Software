package tasks;

import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import java.util.*;

public class TasksOperations {

    private static final Logger log = Logger.getLogger(TasksOperations.class.getName());
    private List<Task> tasks;

    public TasksOperations(ObservableList<Task> tasksList) {
        tasks = new ArrayList<>();
        tasks.addAll(tasksList);
    }

    public Iterable<Task> incoming(Date start, Date end) {
        log.info(start);
        log.info(end);
        ArrayList<Task> incomingTasks = new ArrayList<>();
        for (Task t : tasks) {
            Date nextTime = null;
            if (start.before(end) && !start.equals(end)) {
                if (t.isRepeated() && t.isActive()) {
                    nextTime = t.computeNextTimeAfterRepetedAndActive(start);
                } else if (!t.isRepeated() && start.before(t.getTime()) && t.isActive()) {
                    nextTime = t.getTime();
                }
            }
            if (nextTime != null && (nextTime.before(end) || nextTime.equals(end))) {
                incomingTasks.add(t);
                log.info(t.getTitle());
            }
        }
        return incomingTasks;
    }

    public SortedMap<Date, Set<Task>> calendar(Date start, Date end) {
        Iterable<Task> incomingTasks = incoming(start, end);
        TreeMap<Date, Set<Task>> calendar = new TreeMap<>();

        for (Task t : incomingTasks) {
            Date nextTimeAfter = t.nextTimeAfter(start);
            while (nextTimeAfter != null && (nextTimeAfter.before(end) || nextTimeAfter.equals(end))) {
                if (calendar.containsKey(nextTimeAfter)) {
                    calendar.get(nextTimeAfter).add(t);
                } else {
                    HashSet<Task> oneDateTasks = new HashSet<>();
                    oneDateTasks.add(t);
                    calendar.put(nextTimeAfter, oneDateTasks);
                }
                nextTimeAfter = t.nextTimeAfter(nextTimeAfter);
            }
        }
        return calendar;
    }
}
