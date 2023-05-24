package file_handler;

import javafx.scene.control.Label;
import view.calendar.Controller;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;

public class SaveData {

    private Controller ctr;
    private String readFilePath = "D:\\statisztikai_fajlok\\cal.txt";
    private String writeFilePath = "D:\\statisztikai_fajlok\\cal.txt";
    private ArrayList<String> dataArray = new ArrayList<>();
    private HashMap<String, String> monthNameDecode = new HashMap<>();

    public SaveData(Controller ctr){
        this.ctr = ctr;
        monthNameDecode.put("január", "Jan");
        monthNameDecode.put("február", "Feb");
        monthNameDecode.put("március", "Mar");
        monthNameDecode.put("április", "Apr");
        monthNameDecode.put("május", "Maj");
        monthNameDecode.put("június", "Jun");
        monthNameDecode.put("július", "Jul");
        monthNameDecode.put("augusztus", "Aug");
        monthNameDecode.put("szeptember", "Sep");
        monthNameDecode.put("október", "Okt");
        monthNameDecode.put("november", "Nov");
        monthNameDecode.put("december", "Dec");
        loadData();
        writeData();
    }
    private void loadData(){
        try {
            File inFile = new File(readFilePath);
            Scanner sc = new Scanner(inFile);

            while (sc.hasNextLine()){
                String currentLine = sc.nextLine();
                String resLine = "";
                String[] date = currentLine.split("-")[0].split("\\.");
                String year = date[0];
                String month = date[1];
                String day = date[2];

                String[] restOfData = currentLine.split("-");
                String dayName = restOfData[1];
                String count = restOfData[2];
                String dosage = restOfData[3];

                //IF THE LOADED LINE'S YEAR DATE EQUALS THE CURRENT YEAR
                //AND THE MONT IS MATCHING TOO...
                if(year.equals(ctr.yearLabel.getText()) && monthNameDecode.get(month).equals(ctr.currentMonth)){
                    //TODO: A controllsba a node text-je nem lett állítva, így nem az updatelt értéket veszi ki!
                    dosage = ((Label)ctr.allNodeHash.get("calendar_dosage_"+day)).getText().split(" ")[0];
                    count = ((Label)ctr.allNodeHash.get("calendar_count_"+day)).getText();
                    System.out.println(month+" "+" "+day+" "+count+" "+dosage);
                }

                resLine = year+"."+month+"."+day+"-"+dayName+"-"+count+"-"+dosage;
                dataArray.add(resLine);
            }

        }catch (FileNotFoundException e){
            System.out.println("A fájl nem található!");
        }
    }
    private void writeData(){
        try{
            File outFile = new File(writeFilePath);
            PrintWriter pw = new PrintWriter(outFile);

            for(String s : dataArray){
                System.out.println(s);
                pw.println(s);
            }
            pw.flush();
            pw.close();

        }catch (FileNotFoundException e){
            System.out.println("A fájl nem található");
        }
    }
}
