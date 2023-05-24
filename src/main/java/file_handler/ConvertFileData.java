package file_handler;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class ConvertFileData {

    ArrayList<Character> punctuations = new ArrayList<>();

    //THE ORIGINAL FILE, WHAT'S NEED TO BE CLEANED
    public String originalFilePath = "D:\\statisztikai_fajlok\\original_calendar_data.txt";

    /**
     * One time use only
     */
    public ConvertFileData(){
        // THE PUNCTUATIONS WHAT NEEDS TO BE REMOVED FROM THE ORIGINAL FILE
        punctuations.add('.'); punctuations.add(',');

        loadCalendarFile();
    }
    private String setDataBasedOnCalendarFile(String fileLine){

        //THE CLEANED (FROM PUNCTUATIONS) LINE
        String resFileLine = "";

        for (int i = 0; i < fileLine.length(); i++){

            // GET THE CURRENT CHARACTER FROM THE STRING
            Character currentChar = fileLine.charAt(i);

            //IF IT'S A PUNCTUATION, REMOVE IT
            if(!punctuations.contains(currentChar)){
                //AND IF IT'S NOW A WHITESPACE CHARACTER, APPEND TO: resFileLine
                if(!Character.isWhitespace(currentChar)){
                    resFileLine += currentChar;
                }
                else{
                    //IF IT'S A WHITESPACE CHARACTER, AND THE NEXT CHARACTER IS NOT WHITESPACE
                    //THAN, REPLACE IT WITH "-" MARK, FOR FURTHER SPLIT REGEX
                    if(i+1 < fileLine.length()-1){
                        if(!Character.isWhitespace(fileLine.charAt(i+1))){
                            resFileLine += "-";
                        }
                    }
                }

            }
        }
        String year = "";
        String month = "";
        String day = "";
        String dayName = "";
        String count = "";
        String dosage = "";

        //SPLIT THE CLEANED FILE LINE
        String[] lineList = resFileLine.split("-");
        for(int i = 0; i < lineList.length; i++){
            switch (i){
                case 0: year = lineList[0].strip();
                    break;
                case 1: month = lineList[1].strip();
                    break;
                case 2: day = lineList[2].strip();
                    break;
                case 3: dayName = lineList[3].strip();
                    break;
                case 4: count = lineList[4].strip();
                    break;
                case 5: dosage = lineList[5].strip();
                    break;
            }
        }

        return year+"."+month+"."+day+"-"+dayName+"-"+count+"-"+dosage;
    }
    private void loadCalendarFile(){
        File originalFile = new File(this.originalFilePath);
        File theCleanedFile = new File("D:\\statisztikai_fajlok\\calendar_data.txt");

        //THE FIRST LINE IS COMMENT
        int loadFrom = 1;
        int count = 0;

        //THE ARRAY LIST, THAT CONTAINS THE CLEANED FILE LINES
        ArrayList<String> resultLinesForWriting = new ArrayList<>();

        try{
            Scanner sc = new Scanner(originalFile);
            while (sc.hasNextLine()){
                count++;
                //IF IT'S NOT THE FIRST (COMMENT) LINE, THEN CLEAN THE DATA
                if(count > loadFrom){

                    resultLinesForWriting.add(setDataBasedOnCalendarFile(sc.nextLine()));
                }
                else{
                    sc.nextLine();
                }
            }
            sc.close();

            //WRITE THE CLEANED DATA IN THE NEW FILE
            PrintWriter pw = new PrintWriter(theCleanedFile);
            for(int i = 0; i < resultLinesForWriting.size(); i++){
                //WRITE OUT THE CLEANED LINES IN THE NEW FILE
                pw.println(resultLinesForWriting.get(i));
            }
            pw.close();
        }catch (IOException e){
            System.out.println("There is no such file!");
        }

    }
}
