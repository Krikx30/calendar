package file_handler;

import javafx.scene.Node;
import javafx.scene.control.Label;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoadCalendarFile {
    private HashMap<String, Node> allNodeHash;
    private HashMap<String, String> monthNameDecode;
    private String currentMonth;

    public LoadCalendarFile(String filePath, HashMap<String, Node> nodeHashMap, String currentMonth){

        this.allNodeHash = nodeHashMap;
        monthNameDecode = new HashMap<>();
        this.currentMonth = currentMonth;

        this.resetData();

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

        File calendarDataFile = new File(filePath);
        try{
            Scanner sc = new Scanner(calendarDataFile);


            Pattern fullPattern = Pattern.compile("20[2-9][0-9].[a-zA-Záúóé]{5,10}.[0-9]{1,2}-[a-zéáűúőóüö]{4,}-[?0-9]{1,2}-[?0-9]{1,2}[-]*[a-zA-Z0-9áéúóüőűÁŰÚŐÉÓÜÖ]*");

            Matcher matcher;

            while (sc.hasNextLine()){

                String fullLine = sc.nextLine();

                String[] currentLine = fullLine.split("-");
                String date, dayName, count, dosage, comment;

                //CHECK REGEX
                matcher = fullPattern.matcher(fullLine);

                //IF THE PATTERN MATCHED
                if(matcher.find()){
                    date = currentLine[0];
                    String[] currentDate = date.split("\\.");
                    String year, month, day;
                    year = currentDate[0];
                    month = currentDate[1];
                    day = currentDate[2];

                    dayName = currentLine[1];
                    count = currentLine[2];
                    dosage = currentLine[3];

                    //BECAUSE COMMENT IS NOT ALWAYS GIVEN
                    comment = currentLine.length > 4 ? currentLine[4] : "";


                    //LOAD DATA OF THE CURRENT MONTH OR MONTH, OR THE SELECTED MONTH
                    if(monthNameDecode.get(month) != null){
                        if(((Label) allNodeHash.get("yearLabel")).getText().equals(year) && monthNameDecode.get(month).equals(this.currentMonth)){
                            setDay(day, count, dosage);
                        }

                    }

                }

            }
            sc.close();
        }catch (FileNotFoundException e){
            System.out.println("There is no such file");
        }

    }
    private void setDay(String day, String count, String dosage){
        String countId, dosageId;
        countId = "calendar_count_"+day;
        dosageId = "calendar_dosage_"+day;

        try{
            ((Label)allNodeHash.get(countId)).setText(count);
            ((Label)allNodeHash.get(dosageId)).setText(dosage+" mg");

        }catch (NullPointerException e){
            System.out.println("There is no such id in the hashmap");
        }
    }
    private void resetData(){
        String count = "calendar_count_";
        String dosage = "calendar_dosage_";

        for (int i = 1; i <= 31; i++){
            ((Label)this.allNodeHash.get(count+i)).setText("-");
            ((Label)this.allNodeHash.get(dosage+i)).setText("-");
        }
    }
}
