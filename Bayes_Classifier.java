package main;

import java.util.*;

/**
 * @author Diane Truong
 */
public class Bayes_Classifier {

    ArrayList Class = new ArrayList();
    ArrayList ClassProbabilities = new ArrayList();

    //Database for Probabilities {Attribute/Categories/Sub-Categories/Class_Probabilities}
    ArrayList<ArrayList<ArrayList<ArrayList<Double>>>> Attribute = new ArrayList<ArrayList<ArrayList<ArrayList<Double>>>>();
    ArrayList<Double> SubCategory_Probability = new ArrayList<Double>();

    //Database for Sub-Categories {saveCategories/saveSubCategories/SubCategories}
    ArrayList<ArrayList<String>> saveCategories = new ArrayList<ArrayList<String>>(); //Saves the Sub categories to retrieve its index later

    //Other Variables
    int attributeIndex, classIndex;

    //Finds the classes in the last column
    void getClasses(String[][] Table, int row, int col, String className) {
        int lastColumn = col - 1;
        for (int currentRow = 1; currentRow < row; currentRow++) {
            if (!Class.contains(Table[currentRow][lastColumn])) {
                Class.add(Table[currentRow][lastColumn]);
            }
        }
        System.out.println("\nThe Classes that are in the Table are: " + className + ": " + Arrays.toString(Class.toArray()) + "\n");
        countClasses(Table, row, col - 1, className);
    }

    //Finds the Attributes in the Table
    void getAttributes(String[][] Table, int row, int col, String className) {
        for (int currentCol = 0; currentCol < col - 1; currentCol++) { //col-1 to avoid the last column of Classes

            //Builds the Database for Probabilities's First Level (Attribute/Categories)
            ArrayList<ArrayList<ArrayList<Double>>> Category = new ArrayList<ArrayList<ArrayList<Double>>>();
            Attribute.add(Category);
            ArrayList collectSub_Categories = new ArrayList(); //It temporarily holds the Sub_Categories found (Ex: Age: <=30, 31...40, <40)  

            //Builds the Database of Sub-Categories of each Category (It will be used later to get the index to get the probabilities in the Probabilities Database.)
            ArrayList saveSubCategories = new ArrayList();
            saveCategories.add(saveSubCategories);

            for (int currentRow = 1; currentRow < row; currentRow++) {
                if (!collectSub_Categories.contains(Table[currentRow][currentCol])) { //Classify each attribute data
                    collectSub_Categories.add(Table[currentRow][currentCol]);
                    saveSubCategories.add(Table[currentRow][currentCol]);

                    //Builds the Database for Probabilities's Second Level (Attribute/Categories/Sub-Categories)
                    ArrayList<ArrayList<Double>> Sub_Category = new ArrayList<ArrayList<Double>>();
                    Category.add(Sub_Category);
                }
            }
            System.out.println("\nThe Attributes that are in the Table are: " + Table[0][currentCol] + ": " + Arrays.toString(collectSub_Categories.toArray()));
            classify(Table, collectSub_Categories, row, col, currentCol, className);
        }
        ClassOfTuple(Table, className, row, col);
    }

    void classify(String[][] Table, ArrayList collectSubCategory, int row, int col, int currentCol, String className) {
        for (int n = 0; n < Class.size(); n++) {
            classIndex = n;
            for (int subCateIndex = 0; subCateIndex < collectSubCategory.size(); subCateIndex++) {
                attributeIndex = subCateIndex;
                double AttributeProbability = countAttribute(Table, collectSubCategory, row, col, currentCol, className);
                System.out.println("The probability of " + Table[0][currentCol] + " = " + collectSubCategory.get(attributeIndex) + " | " + className + " = " + Class.get(classIndex) + " is " + AttributeProbability + "");

                //Builds the Database for Probabilities's Third Level (Attribute/Categories/Sub-Categories)
                Attribute.get(currentCol).get(subCateIndex).add(SubCategory_Probability);
                //Builds the Database for Probabilities's Final Level (Attribute/Categories/Sub-Categories/Class_Probabilities)
                SubCategory_Probability.add(AttributeProbability);
            }
        }
    }

    void countClasses(String[][] Table, int row, int col, String className) {
        for (int n = 0; n < Class.size(); n++) {
            int ClassCounter = 0;
            for (int o = 1; o < row; o++) {
                if (Table[o][col].equals(Class.get(n))) { //Goes down the last column 
                    ClassCounter++;
                }
            }
            double probability = findProbability(ClassCounter, row - 1);
            System.out.println("The probability of " + className + " = " + Class.get(n) + " is " + probability);
            ClassProbabilities.add(probability);
        }
    }

    double countAttribute(String[][] Table, ArrayList Attribute, int row, int col, int currentCol, String className) { //Counts the total number of that Attribute-category
        int AttributeCounter = 0;
        int Total = 0;
        int lastColumn = Table.length;
        for (int p = 1; p < row; p++) {
            if (Table[p][currentCol].equals(Attribute.get(attributeIndex))) {
                Total++;
                if (Table[p][col - 1].equals(Class.get(classIndex))) {
                    AttributeCounter++;
                }
            }
        }
        double probability = findProbability(AttributeCounter, Total);
        return probability;
    }

    double findProbability(int Counter, int Total) {
        double probability = (double) Counter / (double) Total;
        return probability;
    }

    //For each Category
    void ClassOfTuple(String[][] Table, String className, int row, int col) {
        System.out.println("");
        double Total = 1;
        for (int currentRow = 1; currentRow < row; currentRow++) { //For each Tuple
            ArrayList<String> decideClass = new ArrayList<>();
            for (int classRow = 0; classRow < Class.size(); classRow++) { //For each Element in Class
                for (int cate = 0; cate < col - 1; cate++) { //For each of Tuple's Category up to the Last Column
                    int SubCatIndex = getSubCatIndex(Table, currentRow, cate); //Get its SubCategoryIndex

                    double Probability = Attribute.get(cate).get(SubCatIndex).get(classRow).get(0); //Final Index is always 0 because there is only one element in the Final ArrayList 
                    Total = Total * Probability;

                }
                //For Each Class
                String XC = Double.toString(ProbabillityXC(Total, classRow));
                decideClass.add(XC);

            }
            int Decision = decideClass.indexOf(Collections.min(decideClass));
            PrintTuple(Table, className, currentRow, col, Decision);
        }
    }

    private int getSubCatIndex(String[][] Table, int currentRow, int cate) { //saveCategories/saveSubCategories/SubCategories
        int SubCatIndex = 0;
        for (int c = cate; c < saveCategories.get(cate).size(); c++) {
            SubCatIndex = saveCategories.get(cate).indexOf(Table[currentRow][cate]);
        }
        return SubCatIndex;
    }

    void PrintTuple(String[][] Table, String className, int row, int col, int Decision) {
        ArrayList Tuple = new ArrayList(); //Temporary ArrayList to easily print out the Tuple
        for (int e = 0; e < col - 1; e++) { //Col-1, to avoid printing last Col
            Tuple.add(Table[row][e]);
        }
        System.out.println("X = " + Arrays.toString(Tuple.toArray()) + " belongs to Class '" + className + ": " + Class.get(Decision) + "'");
    }

    private double ProbabillityXC(double Total, int classRow) {
        double XCProbability = Total * (double) ClassProbabilities.get(classRow);
        return XCProbability;
    }

}
