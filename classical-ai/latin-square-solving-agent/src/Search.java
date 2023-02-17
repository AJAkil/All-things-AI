import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Search {

    ArrayList<Variable> unassignedVariables;
    ArrayList<Variable> trackerList;
    LatinSquare square;
    long nodeCounter = 0;
    long backtracks = 0;

    public Search(LatinSquare square) {
        this.square = square;
        unassignedVariables = new ArrayList<>();
        trackerList = new ArrayList<>();
    }


    /**
     * Pushes the initial unassigned variables to the unassigned list
     */
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


    /**
     * Sets all the dynamic degrees of unassigned variables
     */
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


    /**
     * Used for printing the dynamic degrees of the unassigned variables
     */
    public void printAllDynamicDegrees(){
        System.out.println(this.unassignedVariables.size());
        for (Variable unassignedVariable : this.unassignedVariables) {
            System.out.println("r="+unassignedVariable.getRow()+",c="+unassignedVariable.getCol() + ","+unassignedVariable.getValue() + "," +unassignedVariable.getDynamicDegree());
        }
    }


    /**
     * Updates(increases or decreases) the dynamic degrees of the unassigned neighbors of a cell that is being filled up
     * @param row The row of the variable that is filled up
     * @param col The column of the variable that is filled up
     * @param isBacktracking Parameter that defines whether the method is called during backtracking or not
     */
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


    /**
     * This method reduces the domains of the unassigned neighbors, used for forward checking
     * @param row The row of the variable that is filled up
     * @param col The column of the variable that is filled up
     * @param valuePutOnBoard The value used to fill up the variable cell
     */
    public void reduceNeighborDomains(int row, int col, int valuePutOnBoard){

        // row
        for (int i = 0; i < this.square.getSize(); i++) {

            if (this.square.getBoard()[row][i].getValue() == 0 && i!=col){
                    // checking to see if the value I put is on the domain of unassigned neighbor
                if (this.square.getBoard()[row][i].getDomains().contains(valuePutOnBoard)){
                    this.square.getBoard()[row][i].getDomains().remove((Integer)valuePutOnBoard);
                    this.trackerList.add(this.square.getBoard()[row][i]);
                }
            }
        }

        // column
        for (int i = 0; i < this.square.getSize(); i++) {
            if (this.square.getBoard()[i][col].getValue() == 0 && i!=row){
                // checking to see if the value I put is on the domain of unassigned neighbor
                if (this.square.getBoard()[i][col].getDomains().contains(valuePutOnBoard)){
                    this.square.getBoard()[i][col].getDomains().remove((Integer)valuePutOnBoard);
                    this.trackerList.add(this.square.getBoard()[i][col]);
                }
            }
        }

    }


    /**
     * This method checks if the size of any neighbor domain is zero or not
     * @return true if size is zero, else false
     */
    public boolean isAnyNeighborDomainZero(){
        for (Variable v: this.trackerList){
            if (v.getDomains().size() == 0) return true;
        }
        return false;
    }

//    public void restoreNeighborDomain(int value){
//        //System.out.println("here");
//        for (String s: this.coords){
//            String[] splitted = s.split(",");
//            this.square.getBoard()[Integer.parseInt(splitted[0])][Integer.parseInt(splitted[1])].getDomains().add(value);
//        }
//        //this.trackerList.clear();
//    }


    /**
     * This method checks whether a value is following the constraints of Latin Square or not
     * @param row The row of the variable
     * @param col The column of the variable
     * @param value value is used to check the constraint whether it can be put on row and column of the cell
     * @return True if the constraint is satisfied, else False
     */
    public boolean checkConstraint(int row, int col, int value){
        for (int i = 0; i < this.square.getSize(); i++) {
            if (this.square.getBoard()[row][i].getValue() == value && i!=col) return false;
        }

        for (int i = 0; i < this.square.getSize(); i++) {
            if (this.square.getBoard()[i][col].getValue() == value && i!=row) return false;
        }

        return true;
    }


    /**
     * The main backtracking algorithm that performs the job of searching the final state.
     * Used naive backtracking by looping through the possible domains of a certain variable.
     * The variables are ordered by a certain heuristic. The state and other fields are changed
     * and restored accordingly if the backtrack fails. The nodes and number of backtracks are
     * counted at every recursive call of the function. This is done for reporting purposes.
     * @param heuristic The heuristic that defines how the variables are to be ordered.
     * @return True if we find a solution, else False
     */


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

                if (!heuristic.equalsIgnoreCase("DomainSize")){
                    this.updateDynamicDegree(v.getRow(), v.getCol(), false);
                }

                boolean result = this.backtracking(heuristic);

                if (result) {
//                    this.square.printBoard();
//                    System.out.println();
                    return true;
                }

                // restoring to the previous state
                //this.backtracks++;

                if (!heuristic.equalsIgnoreCase("DomainSize")){
                    this.updateDynamicDegree(v.getRow(), v.getCol(), true);
                }

                this.square.getBoard()[v.getRow()][v.getCol()].setValue(0);

            }else{
                nodeCounter++;
                backtracks++;
            }
        }

        this.unassignedVariables.add(v);
        return false;

    }


    /**
     * The forward checking algorithm that performs the job of searching the final state.
     * Used forward checking by looping through the possible domains of a certain variable.
     * The variables are ordered by a certain heuristic. The domains of the neighbors of a
     * variable that is just being filled are reduced. This domain reduction accounts for faster
     * searching and more pruning of the state space tree. The domains are restored if the recursive
     * call fails. If the size of any domains become zero, then the recursive call is not further
     * considered and the next available domain of the variable in question is explored.
     * The state and other fields are changed and restored accordingly if the backtrack fails.
     * The nodes and number of backtracks are counted at every recursive call of the function.
     * This is done for reporting purposes.
     * @param heuristic The heuristic that defines how the variables are to be ordered.
     * @return True if we find a solution, else False
     */
    public boolean forwardChecking(String heuristic){

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

                // reduction phase
                this.trackerList.clear();
                this.reduceNeighborDomains(v.getRow(), v.getCol(), domain);

                if (!this.isAnyNeighborDomainZero()){

                    if (!heuristic.equalsIgnoreCase("DomainSize")){
                        this.updateDynamicDegree(v.getRow(), v.getCol(), false);
                    }

                    boolean result = this.forwardChecking(heuristic);

                    if (result) {
//                        this.square.printBoard();
//                        System.out.println();
                        return true;
                    }

                    // restoring to the previous state
                    //this.backtracks++;
                    if (!heuristic.equalsIgnoreCase("DomainSize")){
                        this.updateDynamicDegree(v.getRow(), v.getCol(), true);
                    }

                }else{
                    nodeCounter++;
                    backtracks++;
                }

                this.square.getBoard()[v.getRow()][v.getCol()].setValue(0);
                this.setDomains(this.square.getSize());

            }else{
                nodeCounter++;
                backtracks++;
            }
        }

        this.unassignedVariables.add(v);
        return false;
    }


    /**
     * This method returns a suitable comparator based on a certain heuristic. This is
     * used for sorting the variables of the latin square in the backtracking and
     * forward checking search algorithms.
     * @param heuristic The heuristic that defines how the variables are to be ordered.
     */
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

    public long getNodeCounter() {
        return nodeCounter;
    }

    public long getBacktracks() {
        return backtracks;
    }

    public void setNodeCounter(int nodeCounter) {
        this.nodeCounter = nodeCounter;
    }

    public void setBacktracks(int backtracks) {
        this.backtracks = backtracks;
    }
}
