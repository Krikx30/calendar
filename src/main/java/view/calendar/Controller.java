package view.calendar;

import file_handler.LoadCalendarFile;
import file_handler.SaveData;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.paint.Color;

import java.util.regex.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Controller {

    // THIS IS THE MINIMUM YEAR, BECAUSE THERE IS NO DATA BEFORE THIS YEAR
    public final int minYear = 2020;
    public String filePath = "D:\\statisztikai_fajlok\\calendar_data.txt";
    public HashMap<String, Node> allNodeHash;
    // CONTAINS ALL THE MONTHS ID-s
    public ArrayList<String> monthLabelIds = new ArrayList<>();
    @FXML
    public Label yearLabel;

    // BOOLEANS
    // if the calendar locked by the user (Pressing "L")
    private boolean isCalendarLocked = false;

    public  boolean currentlyLoadFile = false;

    //THE CURRENT LABEL? WHERE THE MOUSE HOVER
    public Label mouseOverLabel;

    public  String currentMonth;
    public  String currentDay;
    public int currentYear;


    @FXML
    private void mouseEvent(Event event){
        String target = event.getSource().toString();
        //GET THE OBJECT'S CLASS TYPE
        String objectType = getObjectClass(target);
        String objectId = getObjectId(target);
        String eventType = event.getEventType().toString();

        //IF THE MOUSE ENTERED A LABEL TYPE ELEMENT'S AREA
        if(objectType.equals("label")){
            Label currentLabel = (Label) event.getSource();

            //IF MOUSE ENTERED THE LABEL AREA-----------------------------------------------------
            if(eventType.equals("MOUSE_ENTERED")){
                currentLabel.setStyle("-fx-border-color: RED;");
                mouseOverLabel = currentLabel;
            }

            //IF MOUSE LEAVED THE LABEL AREA------------------------------------------------------
            if(eventType.equals("MOUSE_EXITED")){
                currentLabel.setStyle("");
                mouseOverLabel = null;
            }

            //IF CLICKED ON THE LABEL AREA--------------------------------------------------------
            if(eventType.equals("MOUSE_CLICKED")){
                //IF IT'S A MONTH LABEL
                if(monthLabelIds.contains(currentLabel.getId())){
                    currentMonth = currentLabel.getId();
                }

                //COLOR THE SELECTED MONTH AND LOAD DATA OF THE CURRENT MONTH
                if(monthLabelIds.contains(currentMonth)){
                    colorCurrentLabels(currentLabel,Color.YELLOW);
                    loadDaysData();
                }

                //WHEN THE SAVE LABEL IS CLICKED
                if(currentLabel.getId().equals("calendar_save"))
                {
                    SaveData sd = new SaveData(this);
                    //TODO: MENTENI A BEÁLLÍTOTT HÓNAPOKAT
                }
            }

            //IF MOUSE SCROLLED IN THE LABEL AREA-------------------------------------------------
            if(eventType.equals("SCROLL")){
                //if direction 1 -> SCROLLING UP, IF 0 -> SCROLLING DOWN
                byte direction;
                if(((ScrollEvent)event).getDeltaY() > 0){
                    direction = 1;
                }else{
                    direction = 0;
                }
                // SET YEAR LABEL WHEN SCROLLING
                if(currentLabel.getId().equals("yearLabel")){
                    //GET THE CURRENT YEAR LABEL'S TEXT
                    int currentYear = Integer.parseInt(currentLabel.getText());

                    String resValue; // THE CHANGED YEAR LABEL'S VALUE
                    resValue = direction == 1 ? String.valueOf(++currentYear) : String.valueOf(--currentYear);

                    if(currentYear >= minYear && currentYear <= this.currentYear && !currentlyLoadFile){
                        currentLabel.setText(resValue);
                        this.currentlyLoadFile = true;
                        this.loadDaysData();
                    }

                }

                Pattern nodeIdPattern = Pattern.compile("calendar_[coentdsagu]+_[1-9]{1,2}");
                Matcher nodeMatch = nodeIdPattern.matcher(currentLabel.getId());

                if(nodeMatch.find()){
                    //IF THE VALUE IS SET
                    if(!currentLabel.getText().equals("-")){
                        //THE DATA CAN'T GO BELOW 0
                        int minValue = 0;

                        // SPLIT THE TEXT
                        // IF THE RESULT ARRAY > 1, THEN THE TEXT CONTAINS THE "mg" text
                        String[] currentValue = currentLabel.getText().split(" ");

                        //IF IT'S NOT "?"
                        if(!currentLabel.getText().contains("?")){
                            int value = Integer.parseInt(currentValue[0]);

                            //SET VALUE WHEN SCROLL UP OR DOWN
                            value = direction == 0 ? value -1 : value + 1;

                            //IF THE CALCULATED IS NOT BELOW ZERO
                            if(value >= minValue){
                                currentLabel.setText(currentValue.length > 1 ? (value)+" mg" : String.valueOf(value));
                            }else{
                                currentLabel.setText(currentValue.length > 1 ? "? mg" : "?");
                            }
                        }else{
                            //IF IT'S THE dosage (mg) TEXT
                            if(currentValue.length > 1)
                                currentLabel.setText(direction == 1 ? "0 mg" : "-");
                            else
                                currentLabel.setText(direction == 1 ? "0" : "-");
                        }
                    }
                    //IF THE VALUE IS NOT SET "-"
                    else{
                        String[] splitId = currentLabel.getId().split("_");
                        String witchLabel = splitId[1];
                        switch (witchLabel){
                            case "dosage": currentLabel.setText("0 mg");
                                break;
                            case "count": currentLabel.setText("0");
                                break;
                        }
                    }
                }

            }
        }

    }
    @FXML
    private void keyBoardEvent(KeyEvent event){

        if(event.getEventType().equals(KeyEvent.KEY_PRESSED)){
            String keyCode = event.getCode().toString();

            // LOCK THE CALENDAR
            if(keyCode.equals("L")){
                isCalendarLocked = isCalendarLocked ? false : true;
                System.out.println(isCalendarLocked);
            }
        }

    }
    private String getObjectId(String eventSource){
        int fromIdx = eventSource.indexOf("id=");
        int endIdx = eventSource.indexOf(",", fromIdx+3);
        return eventSource.substring(fromIdx+3,endIdx);
    }
    // Get the STYLECLASS (Button, Label stb.) attribute, which is always look like this
    private String getObjectClass(String eventSource){
        int fromIdx = eventSource.indexOf("styleClass=");
        int endIdx = eventSource.indexOf("]", fromIdx+11);
        return eventSource.substring(fromIdx+11,endIdx);
    }
    private void saveDataToFile(){
        //TODO: megcsinálni a fájlba írást, a kalendárból
        //TODO: és persze megcsinálni az év és hónap váltást, hogy tudjak írni 2022-be is, és vissza.
    }
    private void loadDaysData(){
        LoadCalendarFile lcf = new LoadCalendarFile(filePath, allNodeHash, this.currentMonth);
        currentlyLoadFile = false;
    }
    private void colorCurrentLabels(Label label, Color color){
        for(int i = 0; i < monthLabelIds.size(); i++){
            String currentId = monthLabelIds.get(i);
            if(currentId.equals(label.getId())){
                label.setTextFill(color);
            }else {
                ((Label)allNodeHash.get(currentId)).setTextFill(Color.BLACK);
            }
        }
    }
}