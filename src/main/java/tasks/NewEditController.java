package tasks;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;


public class NewEditController {

    private static Button clickedButton;

    private static final Logger log = Logger.getLogger(NewEditController.class.getName());

    public static void setClickedButton(Button clickedButton) {
        NewEditController.clickedButton = clickedButton;
    }

    public static void setCurrentStage(Stage currentStage) {
        NewEditController.currentStage = currentStage;
    }

    private static Stage currentStage;

    private Task currentTask;
    private ObservableList<Task> tasksList;
    private TasksService service;
    private DateService dateService;


    private boolean incorrectInputMade;
    @FXML
    private TextField fieldTitle;
    @FXML
    private DatePicker datePickerStart;
    @FXML
    private TextField txtFieldTimeStart;
    @FXML
    private  DatePicker datePickerEnd;
    @FXML
    private TextField txtFieldTimeEnd;
    @FXML
    private TextField fieldInterval;
    @FXML
    private CheckBox checkBoxActive;
    @FXML
    private CheckBox checkBoxRepeated;

    private static final String DEFAULT_START_TIME = "8:00";
    private static final String DEFAULT_END_TIME = "10:00";
    private static final String DEFAULT_INTERVAL_TIME = "0:30";

    public void setTasksList(ObservableList<Task> tasksList){
        this.tasksList =tasksList;
    }

    public void setService(TasksService service){
        this.service =service;
        this.dateService = new DateService(service);
    }

    public void setCurrentTask(Task task){
        this.currentTask=task;
        switch (clickedButton.getId()){
            case  "btnNew" : initNewWindow("New Task");
                break;
            case "btnEdit" : initEditWindow("Edit Task");
                break;
            default:
                break;
        }
    }

    @FXML
    public void initialize(){
        log.info("new/edit window initializing");
    }
    private void initNewWindow(String title){
        currentStage.setTitle(title);
        datePickerStart.setValue(LocalDate.now());
        txtFieldTimeStart.setText(DEFAULT_START_TIME);
    }

    private void initEditWindow(String title){
        currentStage.setTitle(title);
        fieldTitle.setText(currentTask.getTitle());
        datePickerStart.setValue(DateService.getLocalDateValueFromDate(currentTask.getStartTime()));
        txtFieldTimeStart.setText(dateService.getTimeOfTheDayFromDate(currentTask.getStartTime()));

        if (currentTask.isRepeated()){
            checkBoxRepeated.setSelected(true);
            hideRepeatedTaskModule(false);
            datePickerEnd.setValue(DateService.getLocalDateValueFromDate(currentTask.getEndTime()));
            fieldInterval.setText(service.getIntervalInHours(currentTask));
            txtFieldTimeEnd.setText(dateService.getTimeOfTheDayFromDate(currentTask.getEndTime()));
        }
        if (currentTask.isActive()){
            checkBoxActive.setSelected(true);

        }
    }
    @FXML
    public void switchRepeatedCheckbox(ActionEvent actionEvent){
        CheckBox source = (CheckBox)actionEvent.getSource();
        if (source.isSelected()){
            hideRepeatedTaskModule(false);
        }
        else if (!source.isSelected()){
            hideRepeatedTaskModule(true);
        }
    }
    private void hideRepeatedTaskModule(boolean toShow){
        datePickerEnd.setDisable(toShow);
        fieldInterval.setDisable(toShow);
        txtFieldTimeEnd.setDisable(toShow);

        datePickerEnd.setValue(LocalDate.now());
        txtFieldTimeEnd.setText(DEFAULT_END_TIME);
        fieldInterval.setText(DEFAULT_INTERVAL_TIME);
    }

    @FXML
    public void saveChanges(){
        Task collectedFieldsTask = collectFieldsData();
        if (incorrectInputMade) return;

        if (currentTask == null){//no task was chosen -> add button was pressed
            tasksList.add(collectedFieldsTask);
        }
        else {
            for (int i = 0; i < tasksList.size(); i++){
                if (currentTask.equals(tasksList.get(i))){
                    tasksList.set(i,collectedFieldsTask);
                }
            }
            currentTask = null;
        }
        TaskIO.rewriteFile(tasksList);
        Controller.editNewStage.close();
    }
    @FXML
    public void closeDialogWindow(){
        Controller.editNewStage.close();
    }

    private Task collectFieldsData(){
        incorrectInputMade = false;
        Task result = null;
        try {
            result = makeTask();
        }
        catch (Exception e){
            incorrectInputMade = true;
            try {
                Stage stage = new Stage();
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/field-validator.fxml"));
                stage.setScene(new Scene(root, 350, 150));
                stage.setResizable(false);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.show();
            }
            catch (IOException ioe){
                log.error("error loading field-validator.fxml");
            }
        }
        return result;
    }
    private Task makeTask() throws TaskException {
        String newTitle = fieldTitle.getText();
        Date startDateWithNoTime = dateService.getDateValueFromLocalDate(datePickerStart.getValue());//ONLY date!!without time
        Date newStartDate = dateService.getDateMergedWithTime(txtFieldTimeStart.getText(), startDateWithNoTime);
        Task result = new Task(newTitle, newStartDate);
        if (checkBoxRepeated.isSelected()){
            Date endDateWithNoTime = dateService.getDateValueFromLocalDate(datePickerEnd.getValue());
            Date newEndDate = dateService.getDateMergedWithTime(txtFieldTimeEnd.getText(), endDateWithNoTime);
            int interval = service.parseFromStringToSeconds(fieldInterval.getText());
            result = createTask(newTitle, newStartDate, newEndDate, interval);
        }
        boolean isActive = checkBoxActive.isSelected();
        result.setActive(isActive);
        return result;
    }

    public Task createTask(String title, Date start, Date end, int interval) throws TaskException {
        Date currentDateTime = new Date();
        if(start.before(currentDateTime) || start.equals(currentDateTime))
            throw new TaskException("start <= current date time");
        if(start.after(end) || start.equals(end))
            throw new TaskException("end <= start");
        if(title.length()>255)
            throw new TaskException("length(title)>255");
        if(title.length()<=0)
            throw new TaskException("length(title)<=0");
        return new Task(title,start,end,interval);
    }
}
