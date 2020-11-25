import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Search {
    ArrayList<Variable> unassignedVariables;
    LatinSquare square;
    int nodeCounter = 0;

    public int getNodeCounter() {
        return nodeCounter;
    }

    public int getBacktracks() {
        return backtracks;
    }

    int backtracks = 0;

    public Search(LatinSquare square) {
        this.square = square;
        unassignedVariables = new ArrayList<>();
    }

    public void setUnassignedVariables(){
        for (int i = 0; i < this.square.getSize(); i++) {
            for (int j = 0; j < this.square.getSize(); j++) {
                if (this.square.getBoard()[i][j].getValue() == 0){
                    unassignedVariables.add(this.square.getBoard()[i][j]);
                }
            }
        }
    }

    /**
     * This function sets the domain for all the unassigned variable
     */
    public void setDomains(int size){

        for (Variable v: unassignedVariables){

            ArrayList<Integer> initialDomains = new ArrayList<>();

            for (int i = 0; i < size; i++) {
                initialDomains.add(i+1);
            }

            int row = v.getRow();
            int col = v.getCol();

            //row
            for (int i = 0; i < this.square.getSize(); i++) {
                int value = this.square.getBoard()[row][i].getValue();
                if (value != 0){
                    initialDomains.remove((Integer) value);
                }
            }

            //col
            for (int i = 0; i < this.square.getSize(); i++) {
                int value = this.square.getBoard()[i][col].getValue();
                if (value != 0){
                    initialDomains.remove((Integer) value);
                }
            }

            v.setDomains(new ArrayList<>());

            for (Integer initialDomain : initialDomains) {
                v.getDomains().add(initialDomain);
            }

//            System.out.println("r,c = "+v.getRow() + "," + v.getCol());
//            System.out.println("DOM="+v.getDomains());
        }



    }

    public void setAllDynamicDegrees(){
        for (Variable unassignedVariable : this.unassignedVariables) {

            int row = unassignedVariable.getRow();
            int col = unassignedVariable.getCol();

            // for the rows
            int sum = 0;

            for (int j = 0; j < this.square.getSize(); j++) {
                if (this.square.getBoard()[row][j].getValue() == 0)
                    sum += 1;
            }

            // for the columns
            for (int j = 0; j < this.square.getSize(); j++) {
                if (this.square.getBoard()[j][col].getValue() == 0)
                    sum += 1;
            }

            unassignedVariable.setDynamicDegree(sum-2);

        }
    }

    public void printAllDynamicDegrees(){
        System.out.println(this.unassignedVariables.size());
        for (Variable unassignedVariable : this.unassignedVariables) {
            System.out.println("r="+unassignedVariable.getRow()+",c="+unassignedVariable.getCol() + ","+unassignedVariable.getValue() + "," +unassignedVariable.getDynamicDegree());
        }
    }

    public void updateDynamicDegree(int row,int col, boolean isBacktracking){
        for (int i = 0; i < this.square.getSize(); i++) {
            if (this.square.getBoard()[row][i].getValue() == 0){
                if ((isBacktracking)) {
                    this.square.getBoard()[row][i].
                            setDynamicDegree(this.square.getBoard()[row][i].getDynamicDegree() + 1);
                } else {
                    this.square.getBoard()[row][i].
                            setDynamicDegree(this.square.getBoard()[row][i].getDynamicDegree() - 1);
                }
            }
        }

        for (int i = 0; i < this.square.getSize(); i++) {
            if (this.square.getBoard()[i][col].getValue() == 0){
                if (isBacktracking){
                    this.square.getBoard()[i][col].
                            setDynamicDegree(this.square.getBoard()[row][i].getDynamicDegree() + 1);
                } else {
                    this.square.getBoard()[i][col].
                            setDynamicDegree(this.square.getBoard()[row][i].getDynamicDegree() - 1);
                }

            }
        }
    }

    public boolean checkConstraint(int row, int col, int value){
        for (int i = 0; i < this.square.getSize(); i++) {
            if (this.square.getBoard()[row][i].getValue() == value && i!=col) return false;
        }

        for (int i = 0; i < this.square.getSize(); i++) {
            if (this.square.getBoard()[i][col].getValue() == value && i!=row) return false;
        }

        return true;
    }

    public boolean backtracking(String heuristic){

        this.nodeCounter++;

        //check to see if the variable list is empty, if so return true
        if (this.unassignedVariables.size() == 0) return true;

        // select a variable by sorting the list of variables
        this.sortByVariableOrdering(heuristic);

        Variable v = this.unassignedVariables.get(0);
        this.unassignedVariables.remove(0);

        // loop through the list of domains
        for (int i = 0; i < v.getDomains().size(); i++) {

            int domain = v.getDomains().get(i);

            if (this.checkConstraint(v.getRow(),v.getCol(), domain)){

                this.square.getBoard()[v.getRow()][v.getCol()].setValue(domain);

                boolean result = this.backtracking(heuristic);

                if (result) {
                    this.square.printBoard();
                    System.out.println();
                    return true;
                }

                // restoring to the previous state
                this.backtracks++;
                this.square.getBoard()[v.getRow()][v.getCol()].setValue(0);
            }
        }


        this.unassignedVariables.add(v);
        return false;

    }

    public void sortByVariableOrdering(String heuristic){
        if (heuristic.equalsIgnoreCase("DomainSize")){
            this.unassignedVariables.sort(new DomainSizeComparator());
        }else if (heuristic.equalsIgnoreCase("DynamicDegree")){
            this.unassignedVariables.sort(new DynamicDegreeComparator());
        }else if(heuristic.equalsIgnoreCase("Breluz")){
            this.unassignedVariables.sort(new BreluzComparator());
        }else if(heuristic.equalsIgnoreCase("Domddeg")){
            this.unassignedVariables.sort(new Domddeg());
        }
    }
}
