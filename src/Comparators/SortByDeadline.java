package Comparators;

import MyObjects.Request;
import java.util.Comparator;

public class SortByDeadline implements Comparator<Request> {
    public int compare(Request a, Request b) {
        return Double.compare(a.getDeadline(), b.getDeadline());
    }
}