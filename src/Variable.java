import java.util.ArrayList;
import java.util.Comparator;

public class Variable {
    private int row;
    private int col;
    private int value;
    private ArrayList<Integer> domains;
    private int staticDegree;

    public static Comparator<Variable> domainSizeComparator = new Comparator<Variable>() {
        @Override
        public int compare(Variable o1, Variable o2) {
            return o1.domains.size() - o2.domains.size();
        }
    };

    public static Comparator<Variable>


}
