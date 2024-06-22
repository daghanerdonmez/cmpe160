/*
* Dağhan Erdönmez 2021400093 18/03/2023
* This project only has Daghan_erdonmez.java file as Java code, and it contains 5 methods.
* Brief explanation of the code:
* The code first reads the content of coordinates.txt file and then gets final and initial stop names from the user.
* It processes the contents of coordinates.txt file and separates them to several arraylists for ease of use further down the code.
* It then checks if the inputs are valid and if the two stations are connected to each other.
* If there is no problem with that part, it then creates a route between them using the
* neighbourStopFinder and the neighbourStopChecker methods.
* It then draws the lines and stops to the map using the nameToCoord method which gives the coordinates of a
* given station on map. Names of the specified stations are also written on top of them.
* */

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.ArrayList;

public class Daghan_erdonmez {
    //Main method reads processes the input file and initializes required methods.
    public static void main(String[] args) throws FileNotFoundException {

        //Checking and creating the source txt file object.
        String coordinates = "coordinates.txt";
        File coordFile = new File(coordinates);
        if (!coordFile.exists()){
            System.out.printf("%s can not be found.",coordFile);
            System.exit(1);
        }
        //Creating a Scanner object to read the file.
        Scanner inputFile = new Scanner(coordFile);

        //Getting initial and final stop name inputs
        Scanner input = new Scanner(System.in);
        String input1 = input.nextLine();
        String input2 = input.nextLine();

        //Creating ArrayLists to store all needed information neatly.
        ArrayList<ArrayList<String>> lineNames = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> allLinesNameStarred = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> allLinesName = new ArrayList<ArrayList<String>>();
        ArrayList<ArrayList<String>> allLinesCoord = new ArrayList<ArrayList<String>>();

        //Using for loop to iterate over the first 20 lines. (Line names, colors and coordinates)
        for (int j = 0; j < 10; j++) {
            String line = inputFile.nextLine();

            //Storing the info in odd numbered lines (Line name, color) in an ArrayList.
            ArrayList<String> hatArrayList = new ArrayList<String>();
            for (String part : line.split(" ")){
                hatArrayList.addAll(Arrays.asList(part.split(",")));
            }
            lineNames.add(hatArrayList);

            //Even numbered lines are names and coordinates of stops. Same process for them.
            String secondLine = inputFile.nextLine();
            ArrayList<String> wholeLineName = new ArrayList<String>();
            ArrayList<String> wholeLineNameStarless = new ArrayList<String>();
            ArrayList<String> wholeLineCoord = new ArrayList<String>();
            for (int i = 0; i < secondLine.split(" ").length; i++){
                //Splitting names from coordinates and storing them seperately.
                if (i % 2 == 0) {
                    if(secondLine.split(" ")[i].startsWith("*")){
                        wholeLineName.add(secondLine.split(" ")[i]);
                        wholeLineNameStarless.add(secondLine.split(" ")[i].substring(1));
                    }
                    else{
                        wholeLineName.add(secondLine.split(" ")[i]);
                        wholeLineNameStarless.add(secondLine.split(" ")[i]);
                    }

                }
                else {
                    wholeLineCoord.add(secondLine.split(" ")[i]);
                }
            }

            //Finally really adding them to general ArrayLists.
            allLinesNameStarred.add(wholeLineName);
            allLinesName.add(wholeLineNameStarless);
            allLinesCoord.add(wholeLineCoord);
        }

        inputFile.close();

        ArrayList<String> inputchecker = new ArrayList<>(); //Used in the if statement below. It will be equal to our initial stop.
        inputchecker.add(input1);
        //Creating the result variable instead of writing long method every time for clean code.
        ArrayList<String> result = recursiveNeighbourChecker(neighbourStopFinder(input1,allLinesName),input1,input2,"",allLinesName);

        if(inputIsValid(input1,input2,allLinesName) == 0){ //If at least one of the stop names is invalid
            System.out.println("No such station names in this map");
        }
        else if((result.equals(inputchecker))&(!input1.equals(input2))){ //If two stations are not connected and not the same
            System.out.println("These two stations are not connected");
        }
        else{
            for(String stop:result){
                System.out.println(stop);
            }
            //Animation Part Begins Here
            StdDraw.setCanvasSize(1024, 482); //Setting Canvas Size
            StdDraw.setXscale(0,1024); //Scaling x and y-axis 1:1
            StdDraw.setYscale(0,482);
            StdDraw.enableDoubleBuffering();
            StdDraw.setFont(new Font("Helvetica",Font.BOLD,8)); //Font size for stop names
            String imagePath = "background.jpg"; //Path of the background image
            int pauseDuration = 300; //Animation dt
            ArrayList<String[]> previousStopCoords = new ArrayList<String[]>();

            for (String s : result) {
                StdDraw.clear(StdDraw.WHITE); //Clear the page on every loop so that updates won't be on top of eachother.
                StdDraw.picture(512, 241, imagePath); //Setting background
                //Taking rgb values from lineNames arraylist
                //(same index corresponds different properties of a single stop in all different arraylists)
                for (int i = 0; i < lineNames.size(); i++) {
                    ArrayList<String> lineinfo = lineNames.get(i);
                    int r = Integer.parseInt(lineinfo.get(1)); //Taking rgb for a line
                    int g = Integer.parseInt(lineinfo.get(2));
                    int b = Integer.parseInt(lineinfo.get(3));
                    for (int j = 0; j < allLinesName.get(i).size(); j++) { //Iterating stops to draw lines between them
                        StdDraw.setPenColor(r, g, b);
                        StdDraw.setPenRadius(0.012);
                        if (j < allLinesName.get(i).size() - 1) {
                            int x1 = Integer.parseInt(allLinesCoord.get(i).get(j).split(",")[0]);
                            int y1 = Integer.parseInt(allLinesCoord.get(i).get(j).split(",")[1]);
                            int x2 = Integer.parseInt(allLinesCoord.get(i).get(j + 1).split(",")[0]);
                            int y2 = Integer.parseInt(allLinesCoord.get(i).get(j + 1).split(",")[1]);
                            StdDraw.line(x1, y1, x2, y2);
                        }
                        //Same iteration is used to draw circles on each stop
                        int x = Integer.parseInt(allLinesCoord.get(i).get(j).split(",")[0]);
                        int y = Integer.parseInt(allLinesCoord.get(i).get(j).split(",")[1]);
                        StdDraw.setPenColor(StdDraw.WHITE);
                        StdDraw.setPenRadius(0.01);
                        StdDraw.filledCircle(x, y, 2.5);
                    }
                    //For loop to write stop names
                    //Can be easily merged into the upper for loop (actually exactly same loop) but
                    //a separata loop because I want names to be on top of lines and circles
                    for (int j = 0; j < allLinesName.get(i).size(); j++) {
                        int x = Integer.parseInt(allLinesCoord.get(i).get(j).split(",")[0]);
                        int y = Integer.parseInt(allLinesCoord.get(i).get(j).split(",")[1]);
                        StdDraw.setPenColor(StdDraw.BLACK);
                        if (allLinesNameStarred.get(i).get(j).startsWith("*")) {
                            StdDraw.text(x, y + 5, allLinesName.get(i).get(j));
                        }
                    }
                }
                //This part draws the orange circles on the past stops.
                String[] currentStopCoords = nameToCoord(s, allLinesName, allLinesCoord);
                StdDraw.setPenColor(StdDraw.PRINCETON_ORANGE);
                for (String[] stopCoord : previousStopCoords) {
                    int x = Integer.parseInt(stopCoord[0]);
                    int y = Integer.parseInt(stopCoord[1]);
                    StdDraw.setPenRadius(0.01);
                    StdDraw.filledCircle(x, y, 2.5);
                }
                //This part draws the orange circle on the current stop.
                int x = Integer.parseInt(currentStopCoords[0]);
                int y = Integer.parseInt(currentStopCoords[1]);
                StdDraw.setPenRadius(0.02);
                StdDraw.filledCircle(x, y, 5);
                previousStopCoords.add(currentStopCoords);
                StdDraw.pause(pauseDuration);

                StdDraw.show();//Updating the screen after every step is done.
            }
        }

    }

    //This method gets a stop name and an arraylist of all lines names and returns the neighbours of that stop as an arraylist
    public static ArrayList<String> neighbourStopFinder(String stop, ArrayList<ArrayList<String>> allLinesName){
        ArrayList<String> neighbours = new ArrayList<String>();
        //Finding the lines that the stop is on.
        for(ArrayList<String> line: allLinesName){
            if(line.contains(stop)){
                //If it is the first stop, return second.
                if((line.indexOf(stop)==0)&(line.size()!=1)){
                    neighbours.add(line.get(1));
                }
                //If it is the last stop, return one before that.
                else if (line.indexOf(stop)==line.size()-1){
                    neighbours.add(line.get(line.size()-2));
                }
                //If it is an intermediate stop, return previous and next stops.
                else{
                    neighbours.add(line.get(line.indexOf(stop)+1));
                    neighbours.add(line.get(line.indexOf(stop)-1));
                }
            }
        }
        return neighbours;
    }

    //This method gets the neighbours of the initial stop, name of the initial stop and the final stop,
    //a previous stop name which is used in the recursive process and the arraylist of all stop names.
    //Returns the full path for that trip as an arraylist.
    public static ArrayList<String> recursiveNeighbourChecker(ArrayList<String> neighbours, String initialStop, String finalStop, String previousStop, ArrayList<ArrayList<String>> allLinesName){
        //This arraylist will be the result.
        ArrayList<String> result = new ArrayList<String>();
        result.add(initialStop);
        //If initial and final stops are the same, return that stop.
        if (initialStop.equals(finalStop)){
            return result;
        }
        //If it comes to an end and still have not found the final stop, adds "deadend" to the result arraylist
        else if((neighbours.size()==1)&(neighbours.contains(previousStop))){
            result.add("deadend");
            return result;
        }
        //This is the main recursive part where it adds the result of all the inner recursions to this recursions result.
        else {
            for (String stop: neighbourStopFinder(initialStop, allLinesName)){
                if (!previousStop.equals(stop)){
                    ArrayList<String> tempresult = recursiveNeighbourChecker(neighbourStopFinder(stop,allLinesName),stop,finalStop,initialStop,allLinesName);
                    if (tempresult.contains(finalStop)){
                        result.addAll(tempresult);
                        break;
                    }
                }
            }
        }
        return result;

    }

    //This method checks if initial and final stop names are on the map. Returns 1 if valid else 0.
    public static int inputIsValid(String stopA, String stopB, ArrayList<ArrayList<String>> allLineNames){
        boolean stopAisValid = false;
        boolean stopBisValid = false;
        for (ArrayList<String> line: allLineNames){
            for (String stop : line){
                if (stopA.equals(stop)){
                    stopAisValid = true;
                }
                if (stopB.equals(stop)){
                    stopBisValid = true;
                }
            }
        }
        if (stopAisValid & stopBisValid){
            return 1;
        }
        else{
            return 0;
        }
    }

    //This method returns the coordinates of a stop as an array (x,y) when given its name
    //It also needs allLineNames and allLinesCoord arrays as arguments to locate the stop
    public static String[] nameToCoord(String stop, ArrayList<ArrayList<String>> allLineNames, ArrayList<ArrayList<String>> allLinesCoord){
        for(int i = 0; i<allLineNames.size(); i++){
            for(int j = 0; j < allLineNames.get(i).size(); j++){
                if(allLineNames.get(i).get(j).equals(stop)){
                    return allLinesCoord.get(i).get(j).split(",");
                }
            }
        }
        return null;
    }
}