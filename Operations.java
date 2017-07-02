package main;

import java.io.*;
import java.util.*;

/**
 * @author Diane Truong
 */
public class Operations {
    int col, row;
    String className = "";
    String[] attributeNames = new String[col];
    Bayes_Classifier Bayes = new Bayes_Classifier(); 
    
    //Read from File
    void readFile(File file) throws FileNotFoundException, IOException {  
        countRows(file);
        countColumns(file);
        String[][] Table = new String[row][col];
        
        getAttributeNames(file);
        System.out.println("Attribute names are stored."); //Test: Confirms that attributes names are saved.
        saveToMatrix(file, Table);
    }
    
    //Count Rows in file
    void countRows(File file) throws IOException {
        Scanner readFile = new Scanner((file));
         while (readFile.hasNextLine()) {         
            row++;
            readFile.nextLine();
        }
        System.out.println("Rows: " + row); //Test: Number of Rows
        readFile.close();  
    }
    
    //Count Columns in file
    void countColumns(File file) throws IOException {
        BufferedReader readFile =  new BufferedReader(new FileReader(file));
        String firstLine = readFile.readLine();
        String[] countColumns = firstLine.split(" ");
        col = countColumns.length;
        System.out.println("Columns: " + col); //Test: Number of Columns
        readFile.close();  
    }
    
    //Get attribute names
    void getAttributeNames(File file) throws IOException {
        BufferedReader readFile =  new BufferedReader(new FileReader(file));
        String firstLine = readFile.readLine();
        attributeNames = firstLine.split(" ");
        className = attributeNames[col-1];
        System.out.println("\nAttribute Names Array: " + Arrays.toString(attributeNames)); //Test: Attribute Names Array
        readFile.close();
    }
    
    //Save to Matrix
    void saveToMatrix(File file, String[][] Table) throws IOException {
        Scanner readFile =  new Scanner(file);
        for(int i = 0; i < row; i++) {
            for(int j = 0; j < col; j++) {
                Table[i][j] = readFile.next();
            }
        }
        System.out.println("\nTable from File:"); //Test: Table from File
        printMatrix(Table);
        splitRows(Table);
        Bayes.getClasses(Table, row, col, className);
        Bayes.getAttributes(Table, row, col, className);
        readFile.close();
    }
    
    //Split rows from 2D Array
    void splitRows(String[][] Table) {
        System.out.println("\n\nSplitting and saving the Tuples into separate Arrays:\n");
        ArrayList Data = new ArrayList();
        for (int k = 0; k < row; k++) {
            ArrayList Tuple = new ArrayList();
            for (int l = 0; l < col-1; l++) {
                Tuple.add(Table[k][l]);
            }
            if (k != 0) { //Avoid printing the first row as a Tuple
                System.out.println("X = " + Arrays.toString(Tuple.toArray())); //Test: Print current Tuple ArrayList to make sure it is correct
            } else {
                System.out.println(Arrays.toString(Tuple.toArray()));
            }
        }
    }
    
    //Print matrix
    public void printMatrix(String[][] matrix) {
        for (int i = 0; i < matrix.length; i++) {
            System.out.print("\nRow " + i + ":\t");
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.format("%-17s", matrix[i][j]);
            }
        }
    }
}
