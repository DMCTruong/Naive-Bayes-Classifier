package main;

import java.io.*;
import java.net.URL;
import java.util.Scanner;

/**
 * @author Diane Truong
 */
public class Run {

    public static void main(String[] args) throws FileNotFoundException, IOException {
        System.out.println("Naive bayes Classifier Program \nHello and welcome, please press 1 if you would like to enter the file path to the input File.");
        System.out.println("Or press 2 to use the file path to the input file provided in the system.\n");
        Scanner in = new Scanner(System.in);
        String input = in.next();

        String filepath = "";
        switch (input) {
            case "1":
                System.out.println("Please enter the path to the input file.");
                String skipFirstLine = in.nextLine();
                String getfilepath = in.nextLine();
                File file1 = new File(getfilepath);
                RunProgram(file1);
                break;
            case "2":
                String line;
                String pidInfo = "";
                Process p = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe");
                BufferedReader getPid = new BufferedReader(new InputStreamReader(p.getInputStream()));

                while ((line = getPid.readLine()) != null) {
                    pidInfo += line;
                }
                getPid.close();

                if (pidInfo.contains("netbeans64.exe")) {
                    filepath = "src\\main\\table.txt";
                    File file2 = new File(filepath);
                    RunProgram(file2);
                } else {
                    filepath = "table.txt";
                    File file2 = new File(filepath);
                    RunProgram(file2);
                }
                break;
            default:
                System.out.println("Sorry, invalid input. Please press either 1 or 2 to proceed.");
                break;
        }
        System.out.println("\nEnd of Program");
    }

    public static void RunProgram(File file) throws IOException {
        Operations op = new Operations();
        try {
            BufferedReader readFile = new BufferedReader(new FileReader(file)); //To test in advance if the path to the file is correct
            op.readFile(file);
        } catch (FileNotFoundException e) {
            System.out.println("Sorry, the file is not found. Please try again.");
        }
    }

}
