import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class Search {
    ArrayList<Variable> unassignedVariables;
    ArrayList<Variable> trackerList;
    LatinSquare square;
    int nodeCounter = 0;
    int backtracks = 0;
    int track = 0;

    public int getNodeCounter() {
        return nodeCounter;
    }

    public int getBacktracks() {
        return backtracks;
    }

    public Search(LatinSquare square) {
        this.square = square;
        unassignedVariables = new ArrayList<>();
        trackerList = new ArrayList<>();
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


    public void reduceNeighborDomains(int row, int col, int valuePutOnBoard){
        this.trackerList.clear();
        track++;
        System.out.println("track in domain-reduction "+track);
        // row
        for (int i = 0; i < this.square.getSize(); i++) {

            if (this.square.getBoard()[row][i].getValue() == 0){
                    // checking to see if the value I put is on the domain of unassigned neighbor
                if (this.square.getBoard()[row][i].getDomains().contains(valuePutOnBoard)){
                    this.trackerList.add(this.square.getBoard()[row][i]);
                    this.square.getBoard()[row][i].getDomains().remove((Integer)valuePutOnBoard);
                }
            }
        }

        // column
        for (int i = 0; i < this.square.getSize(); i++) {
            if (this.square.getBoard()[i][col].getValue() == 0){
                // checking to see if the value I put is on the domain of unassigned neighbor
                if (this.square.getBoard()[i][col].getDomains().contains(valuePutOnBoard)){
                    this.trackerList.add(this.square.getBoard()[i][col]);
                    this.square.getBoard()[i][col].getDomains().remove((Integer)valuePutOnBoard);
                }
            }
        }

    }

    public boolean isAnyNeighborDomainZero(){
        for (Variable v: this.trackerList){
            if (v.getDomains().size() == 0) return true;
        }
        return false;
    }

    public void restoreNeighborDomain(int value){
        for (Variable v: this.trackerList){
            v.getDomains().add(value);
        }
        this.trackerList.clear();
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
        //System.out.println(nodeCounter);

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
//                    System.out.println("WTF");
//                    this.square.printBoard();
//                    System.out.println();
                    return true;
                }

                // restoring to the previous state
                this.backtracks++;

                if (!heuristic.equalsIgnoreCase("DomainSize")){
                    this.updateDynamicDegree(v.getRow(), v.getCol(), true);
                }

                this.square.getBoard()[v.getRow()][v.getCol()].setValue(0);


            }
        }

        this.unassignedVariables.add(v);
        return false;

    }


    public int getTrack() {
        return track;
    }

    public boolean forwardChecking(String heuristic){

        this.nodeCounter++;
        System.out.println("SIZE = "+unassignedVariables.size());

        //check to see if the variable list is empty, if so return true
        if (this.unassignedVariables.size() == 0) return true;

        // select a variable by sorting the list of variables
        this.sortByVariableOrdering(heuristic);

        Variable v = this.unassignedVariables.get(0);
        //System.out.println("r,c=" + v.getRow()+","+v.getCol());
        this.unassignedVariables.remove(0);

        // loop through the list of domains
        for (int i = 0; i < v.getDomains().size(); i++) {

            int domain = v.getDomains().get(i);

            if (this.checkConstraint(v.getRow(),v.getCol(), domain)){

                this.square.getBoard()[v.getRow()][v.getCol()].setValue(domain);

                // reduction phase
                this.reduceNeighborDomains(v.getRow(), v.getCol(), domain);

                if (this.isAnyNeighborDomainZero()){

                    this.square.getBoard()[v.getRow()][v.getCol()].setValue(0);
                    this.restoreNeighborDomain(domain);
                    track--;
                    System.out.println("track if domain is zero "+track);

                }else{

                    if (!heuristic.equalsIgnoreCase("DomainSize")){
                        this.updateDynamicDegree(v.getRow(), v.getCol(), false);
                    }

                    boolean result = this.forwardChecking(heuristic);

                    if (result) {
                        System.out.println("WTF");
//                        this.square.printBoard();
//                        System.out.println();
                        return true;
                    }

                    // restoring to the previous state
                    this.backtracks++;
                    this.restoreNeighborDomain(domain);

                    if (!heuristic.equalsIgnoreCase("DomainSize")){
                        this.updateDynamicDegree(v.getRow(), v.getCol(), true);
                    }

                    this.square.getBoard()[v.getRow()][v.getCol()].setValue(0);
                }
            }
        }

        this.unassignedVariables.add(v);
        System.out.println("size="+this.unassignedVariables.size());
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

    public void setNodeCounter(int nodeCounter) {
        this.nodeCounter = nodeCounter;
    }

    public void setBacktracks(int backtracks) {
        this.backtracks = backtracks;
    }
}
