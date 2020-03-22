package tasks;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import org.apache.log4j.Logger;
import org.controlsfx.control.Notifications;

import java.util.Date;

public class Notificator extends Thread {

    private static final long MILISECONDS_IN_SEC = 1000;
    private static final int SECONDS_IN_MIN = 60;

    private static final Logger log = Logger.getLogger(Notificator.class.getName());

    private ObservableList<Task> tasksList;

    public Notificator(ObservableList<Task> tasksList){
        this.tasksList=tasksList;
    }

    private void isRepeated(Task t, Date currentDate)
    {
        Date next = t.nextTimeAfter(currentDate);
        long currentMinute = getTimeInMinutes(currentDate);
        long taskMinute = getTimeInMinutes(next);
        if (currentMinute == taskMinute){
            showNotification(t);
        }
    }

    private boolean currentDateSameAsTaskTime(Task t, Date currentDate)
    {
        return getTimeInMinutes(currentDate) == getTimeInMinutes(t.getTime());
    }
    @Override
    public void run() {
        Date currentDate = new Date();
        while (true) {
            for (Task t : tasksList) {
                if (t.isActive() && t.isRepeated() && t.getEndTime().after(currentDate)) {
                        isRepeated(t,currentDate);
                }
                if (t.isActive() && !t.isRepeated() && currentDateSameAsTaskTime(t,currentDate)){
                        showNotification(t);
                    }
                }
            try {
                Thread.sleep(MILISECONDS_IN_SEC*SECONDS_IN_MIN);
            } catch (InterruptedException e) {
                log.error("thread interrupted exception: "+ e.getMessage());
                Thread.currentThread().interrupt();
            }
            currentDate = new Date();
        }
    }
    public static void showNotification(Task task){
        log.info("push notification showing");
        Platform.runLater(() ->
            Notifications.create().title("Task reminder").text("It's time for " + task.getTitle()).showInformation()
        );
    }
    private static long getTimeInMinutes(Date date){
        return date.getTime()/MILISECONDS_IN_SEC/SECONDS_IN_MIN;
    }
}
