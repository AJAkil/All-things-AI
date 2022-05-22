import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Objects;

public class Variable {

    private int row;
    private int col;
    private int value;
    private ArrayList<Integer> domains;
    private int staticDegree;
    private int dynamicDegree;

    public int getDynamicDegree() {
        return dynamicDegree;
    }

    public void setDynamicDegree(int dynamicDegree) {
        this.dynamicDegree = dynamicDegree;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Variable variable = (Variable) o;
        return row == variable.row &&
                col == variable.col &&
                value == variable.value &&
                staticDegree == variable.staticDegree &&
                dynamicDegree == variable.dynamicDegree &&
                Objects.equals(domains, variable.domains);
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, value, domains, staticDegree, dynamicDegree);
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public ArrayList<Integer> getDomains() {
        return domains;
    }

    public void setDomains(ArrayList<Integer> domains) {
        this.domains = domains;
    }

    public void setStaticDegree(int staticDegree) {
        this.staticDegree = staticDegree;
    }

}
