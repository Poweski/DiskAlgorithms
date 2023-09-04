package Helpful;

import MyObjects.Request;

public class MovesCalculator {

    public static int countCylinderMoves(Request req1, Request req2) {
        return Math.abs(req1.getCylinderID() - req2.getCylinderID());
    }
    public static int countPlatterMoves(Request req1, Request req2) {
        return Math.abs(req1.getPlatterID() - req2.getPlatterID());
    }
    public static int countSetMoves(Request req1, Request req2) {
        return Math.abs(req1.getSetID() - req2.getSetID());
    }
}
