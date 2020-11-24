import java.util.Arrays;

public class LatinSquare {
    private int size;
    Variable [][] board;

    public int getSize() {
        return size;
    }

    public Variable[][] getBoard() {
        return board;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void initiateLatinSquare(String boardData){

        this.board = new Variable[this.size][this.size];
        String[] s = boardData.split("\\$");
        int row = 0;

        for (String value : s) {
            String[] cellValues = value.split(",");

            for (int i = 0; i < cellValues.length; i++) {

                Variable v = new Variable();
                v.setRow(row);
                v.setCol(i);
                v.setValue(Integer.parseInt(cellValues[i]));
                v.setDomains(this.size);
                v.setStaticDegree((this.size-1)*2);

                this.board[row][i] = v;
            }
            row++;
        }

    }

    public void setAllDynamicDegrees(){
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                if (this.board[i][j].getValue() == 0){

                }
            }
        }
    }



    public void printBoard(){
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                System.out.print(board[i][j]+",");
            }
            System.out.println();
        }
    }

    /**
     * This method checks if the current state of the latin square is complete or not
     * @return true or false based ont the state of the latin square
     */
    public boolean isLatinSquareComplete(){

        int reqSum = this.getReqSum();

        for (int i = 0; i < this.size; i++) {

            int temp = reqSum;

            for (int j = 0; j < this.size; j++) {
                temp -= this.board[i][j].getValue();
            }

            if (temp!=0) return false;
        }

        for (int i = 0; i < this.size; i++) {

            int temp = reqSum;

            for (int j = 0; j < this.size; j++) {
                temp -= this.board[j][i].getValue();
            }

            if (temp!=0) return false;
        }

        return true;
    }

    /**
     * This method gives the required sum for the rows or columns of the latin square
     * @return
     */
    public int getReqSum(){

        int sum = 0;

        for (int i = 1; i <=this.size ; i++) {
            sum += i;
        }

        return sum;
    }
}
