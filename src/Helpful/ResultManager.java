package Helpful;

import MyObjects.Request;

import java.util.ArrayList;

public class ResultManager {

    public static void presentResults(String name, ArrayList<Request> requestsList, int time,
                                      int cylinderChangeMoves, int setChangeMoves, int platterChangeMoves) {

        long wholeWaitingTime = 0;
        int servedBeforeDeadline = 0;
        int numberOfRequests = requestsList.size();
        int numberOfRequestsWithDeadline = 0;

        for (Request request: requestsList) {
            wholeWaitingTime += request.getWaitingTime();
            int momentOfResponse = request.getMomentOfNotification() + request.getWaitingTime();
            if (request.getDeadline() != Double.POSITIVE_INFINITY) {
                numberOfRequestsWithDeadline++;
                if (momentOfResponse <= request.getDeadline()) {
                    servedBeforeDeadline++;
                }
            }
        }

        float percentageDeadline = (float)servedBeforeDeadline*100/numberOfRequestsWithDeadline;

        System.out.printf("\n" + name + " RESULTS:");
        System.out.printf("\nTotal time: %,d",time);
        System.out.printf("\nAverage waiting time: %,d", wholeWaitingTime/numberOfRequests);
        if (numberOfRequestsWithDeadline != 0) {
            System.out.printf("\n%% of requests served before deadline: %,.2f%%", percentageDeadline);
        }
        else {
            System.out.print("\n% of requests served before deadline: -");
        }
        System.out.printf("\nMoves in order to change platter: %,d", platterChangeMoves);
        System.out.printf("\nMoves in order to change set: %,d", setChangeMoves);
        System.out.printf("\nMoves in order to change cylinder: %,d\n", cylinderChangeMoves);
    }
}