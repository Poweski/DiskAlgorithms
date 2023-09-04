package Comparators;

import MyObjects.Request;
import java.util.Comparator;

public class SortByMomentOfNotification implements Comparator<Request> {
    public int compare(Request a, Request b) {
        return Integer.compare(a.getMomentOfNotification(), b.getMomentOfNotification());
    }
}