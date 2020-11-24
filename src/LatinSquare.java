public class LatinSquare {
    private final int size;
    Variable [][] board;

    LatinSquare(int size){
        this.size = size;
        board = new Variable[size][size];
    }

    public void initiateLatinSquare(){

    }

    public boolean isLatinSquareComplete(){

        int reqSum = this.getReqSum();

        for (int i = 0; i < this.size; i++) {

            int temp = reqSum;

            for (int j = 0; j < this.size; j++) {
                temp -= this.board[i][j].value;
            }

            if (temp!=0) return false;
        }

        for (int i = 0; i < this.size; i++) {

            int temp = reqSum;

            for (int j = 0; j < this.size; j++) {
                temp -= this.board[j][i].value;
            }

            if (temp!=0) return false;
        }

        return true;
    }

    public int getReqSum(){

        int sum = 0;

        for (int i = 1; i <=this.size ; i++) {
            sum += i;
        }

        return sum;
    }
}
