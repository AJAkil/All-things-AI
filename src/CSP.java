import java.util.ArrayList;

public class CSP {
    ArrayList<Variable> unassignedVariables;
    LatinSquare square;

    public CSP(LatinSquare square) {
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

    public void updateDynamicDegree(int row,int col){
        for (int i = 0; i < this.square.getSize(); i++) {
            if (this.square.getBoard()[row][i].getValue() == 0){
                this.square.getBoard()[row][i].setDynamicDegree(this.square.getBoard()[row][i].getDynamicDegree()-1);
            }
        }

        for (int i = 0; i < this.square.getSize(); i++) {
            if (this.square.getBoard()[i][col].getValue() == 0){
                this.square.getBoard()[i][col].setDynamicDegree(this.square.getBoard()[row][i].getDynamicDegree()-1);
            }
        }
    }
}
