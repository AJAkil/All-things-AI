import java.util.Comparator;


/**
 * used for sorting in ascending order of domain sizes
 */
class DomainSizeComparator implements Comparator<Variable>{
    @Override
    public int compare(Variable o1, Variable o2) {
        return o1.getDomains().size() - o2.getDomains().size();
    }
}


/**
 * used for sorting in descending order of dynamic degree, that is max degree is first
 */
class DynamicDegreeComparator implements Comparator<Variable>{
    @Override
    public int compare(Variable o1, Variable o2) {
        return o2.getDynamicDegree() - o1.getDynamicDegree();
    }
}


/**
 * The variable chosen is the one with the smallest domain.
 * Ties are broken by choosing the variable with smallest domain and maximum forward degree.
 */
class BreluzComparator implements Comparator<Variable>{
    @Override
    public int compare(Variable o1, Variable o2) {
        if (o1.getDomains().size() == o2.getDomains().size()){
            return o2.getDynamicDegree() - o1.getDynamicDegree();
        }else return o1.getDomains().size() - o2.getDomains().size();
    }
}


/**
 * The variable chosen is the one that minimizes the ratio of domain size to forward degree
 * (i.e.the number of adjacent uninstantiated variables).
 */
class Domddeg implements Comparator<Variable>{
    @Override
    public int compare(Variable o1, Variable o2) {

        float ratio1=(float) o1.getDomains().size()/o1.getDynamicDegree();
        float ratio2=(float) o2.getDomains().size()/o2.getDynamicDegree();

        if(ratio1>ratio2) return 1;
        else if(ratio1<ratio2) return -1;
        return 0;
    }
}
