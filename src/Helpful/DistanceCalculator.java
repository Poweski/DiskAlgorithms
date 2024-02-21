package Helpful;

import MyObjects.Disc;
import MyObjects.Request;

public class DistanceCalculator {

    public static int getDifferenceInTimeBetweenTwoSegments
            (int address1, int address2, Disc disc, int platterChangeTime,
             int cylinderChangeTime, int blockChangeTime) {

        int[] pos1 = disc.getCylinderBlockPlatterNumber(address1);
        int cylinderID1 = pos1[0];
        int blockID1 = pos1[1];
        int platterID1 = pos1[2];

        int[] pos2 = disc.getCylinderBlockPlatterNumber(address2);
        int cylinderID2 = pos2[0];
        int blockID2 = pos2[1];
        int platterID2 = pos2[2];

        return Math.abs(platterID1-platterID2) * platterChangeTime
                + Math.abs(cylinderID1-cylinderID2) * cylinderChangeTime
                + Math.abs(blockID1-blockID2) * blockChangeTime;
    }

    public static int getDifferenceInTimeBetweenTwoRequests
            (Request req1, Request req2, int platterChangeTime,
             int cylinderChangeTime, int blockChangeTime) {

        if (req1 == null) {
            return req2.getPlatterID() * platterChangeTime
                    + req2.getSetID() * blockChangeTime
                    + req2.getCylinderID() * cylinderChangeTime;
        }

        if (req2 == null) {
            return req1.getPlatterID() * platterChangeTime
                    + req1.getSetID() * blockChangeTime
                    + req1.getCylinderID() * cylinderChangeTime;
        }

        int cylinderID1 = req1.getCylinderID();
        int blockID1 = req1.getSetID();
        int platterID1 = req1.getPlatterID();
        int cylinderID2 = req2.getCylinderID();
        int blockID2 = req2.getSetID();
        int platterID2 = req2.getPlatterID();

        return Math.abs(platterID1-platterID2) * platterChangeTime
                + Math.abs(cylinderID1-cylinderID2) * cylinderChangeTime
                + Math.abs(blockID1-blockID2) * blockChangeTime;
    }
}
