package view.calendar;

import file_handler.ConvertFileData;
import file_handler.LoadCalendarFile;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.*;

import javafx.scene.control.Label;

public class MainApplication extends Application {
    public Stage stage;
    public Controller ctr;
    public FXMLLoader loader;
    public Parent root;

    //CONTAINS ALL THE NODES (ELEMENTS OF THE FXML) WITH THEIR IDS
    public HashMap<String, Node> allNodeHash = new HashMap<>();

    public ArrayList<Node> allNodeList = new ArrayList<>();
    public HashMap<String, String> monthsDecode = new HashMap<>();

    //THE FILE, THAT CONTAINS THE DATA
    private String calendarDataPath = "D:\\statisztikai_fajlok\\calendar_data.txt";

    
    //LOAD THE LABELS INTO A HASHMAP (allNodeHash)
    private void getNodes(Parent node){
        allNodeHash.put(node.getId(),node);
        if(node.getChildrenUnmodifiable() != null){
            List<Node> currentList = node.getChildrenUnmodifiable().stream().toList();
            for(int i = 0; i < currentList.size(); i++){
                this.getNodes(((Parent)currentList.get(i)));
            }
        }



    }
    private void initYearAndCurrentDay(){

        // GET CURRENT TIME
        String currentTime = Calendar.getInstance().getTime().toString();

        //SET THE YEAR LABEL IN THE MIDDLE OF THE CALENDAR
        Label yearlabel = (Label)allNodeHash.get("yearLabel");
        yearlabel.setText(currentTime.split(" ")[currentTime.split(" ").length-1]);
        ctr.currentYear = Integer.parseInt(currentTime.split(" ")[currentTime.split(" ").length-1]);

        //GET CURRENT MONTH AND DAY
        ctr.currentMonth = currentTime.split(" ")[1];

        //IF THE FIRST CHAR OF THE DAY IS NOT 0 -> LIKE 01, OR 02 ETC.
        if(!(String.valueOf(currentTime.split(" ")[2].charAt(0)).equals("0"))){
            ctr.currentDay = currentTime.split(" ")[2];
        }else{
            //IF IT'S 0, THEN GET ONLY THE SECOND CHAR, BECAUSE IN THE FXML, THE IDS USE ONY 1, 2, 3 ETC.
            ctr.currentDay = String.valueOf (currentTime.split(" ")[2].charAt(1));
        }


        //SET THE CURRENT MONTH AND DAY
        //BUT BEFORE THAT, ADD VALUES FOR FURTHER USE IN CONTROL CLASS
        ctr.monthLabelIds.add("Jan");ctr.monthLabelIds.add("Feb");ctr.monthLabelIds.add("Mar");
        ctr.monthLabelIds.add("Apr");ctr.monthLabelIds.add("Maj");ctr.monthLabelIds.add("Jun");
        ctr.monthLabelIds.add("Jul");ctr.monthLabelIds.add("Aug");ctr.monthLabelIds.add("Sep");
        ctr.monthLabelIds.add("Okt");ctr.monthLabelIds.add("Nov");ctr.monthLabelIds.add("Dec");

        Label cMonth = (Label) (allNodeHash.get(ctr.currentMonth));
        //cMonth.setTextFill(Color.YELLOW);
        Label cDay = (Label) (allNodeHash.get("calendar_day_"+ctr.currentDay));
        cDay.setTextFill(Color.ORANGE);
    }
    private void loadCalendarData(){
        LoadCalendarFile lcf = new LoadCalendarFile(calendarDataPath, allNodeHash, ctr.currentMonth);
    }
    private void initCalendar(){

        //GET ALL NODES
        getNodes(this.root);

        // GIVE NODE HASH_MAP TO CONTROLLER, FOR FURTHER USE
        ctr.allNodeHash = this.allNodeHash;

        //LOAD THE ORIGINAL FILE TO CLEAN THE DATA   //
        //JUST FOR ONCE, AND NEVER AGAIN                //
        //ConvertFileData cfd = new ConvertFileData();

        initYearAndCurrentDay();

        //LOAD THE CLEANED DATA IN THE CALENDAR, WHEN THE APPLICATION STARTS
        this.loadCalendarData();

    }
    @Override
    public void start(Stage stage) throws IOException {
        this.stage = stage;

        this.loader = new FXMLLoader(getClass().getResource("view.fxml"));
        this.root = loader.load();

        //GET THE CONTROLLER CLASS
        ctr = loader.getController();

        Scene scene = new Scene(root,1024, 768);
        this.stage.setTitle("Kalendárium");
        this.stage.setScene(scene);

        //INIT THE CALENDAR'S DATA LOAD
        initCalendar();

        //GIVE THE FOCUS TO THE ROOT, BECAUSE KEYBOARD EVENTS
        root.requestFocus();

        this.stage.show();
    }
    public static void main(String[] args) {
        launch();
    }
}