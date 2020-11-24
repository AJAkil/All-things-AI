import java.util.Comparator;

/**
 * used for sorting in ascending order of domain sizes
 */
class DomainSizeComp implements Comparator<Variable>{
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


class BreluzComparator implements Comparator<Variable>{
    @Override
    public int compare(Variable o1, Variable o2) {
        return 0;
    }
}


class Domddeg implements Comparator<Variable>{
    @Override
    public int compare(Variable o1, Variable o2) {
        return 0;
    }
}
