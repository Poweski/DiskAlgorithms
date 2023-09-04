package Algorithms;

import Comparators.*;
import MyObjects.*;
import Helpful.*;

import java.util.ArrayList;

public class FCFS {
    private Request lastlyExecutedRequest = null;
    private final ArrayList<Request> listOfDeadRequests = new ArrayList<>();
    private final ArrayList<Request> queueOfRequests;
    private final int requestProcessingTime;
    private final int cylinderChangeTime;
    private final int setChangeTime;
    private final int platterChangeTime;
    private int cylinderNumberOfMoves = 0;
    private int platterNumberOfMoves = 0;
    private int setNumberOfMoves = 0;
    private int time = 0;

    public FCFS (Disc disc, int cylinderChangeTime, int setChangeTime, int platterChangeTime, int requestProcessingTime) {

        this.setChangeTime = setChangeTime;
        this.cylinderChangeTime = cylinderChangeTime;
        this.platterChangeTime = platterChangeTime;
        this.requestProcessingTime = requestProcessingTime;

        queueOfRequests = TableManager.convertRequestTableToArrayList(disc.getDisc());
        queueOfRequests.sort(new SortByMomentOfNotification());

        runTheSimulation();

        ResultManager.presentResults("FCFS", listOfDeadRequests, time,
                cylinderNumberOfMoves, setNumberOfMoves, platterNumberOfMoves);
    }

    private void runTheSimulation() {

        Request nextRequest = findNextRequest();

        while (nextRequest != null) {

            if (nextRequest.getMomentOfNotification() > time)
                time = nextRequest.getMomentOfNotification();

            time += DistanceCalculator.getDifferenceInTimeBetweenTwoRequests(lastlyExecutedRequest,
                    nextRequest, platterChangeTime, cylinderChangeTime, setChangeTime);

            if (lastlyExecutedRequest != null) {
                cylinderNumberOfMoves += MovesCalculator.countCylinderMoves(lastlyExecutedRequest,nextRequest);
                platterNumberOfMoves += MovesCalculator.countPlatterMoves(lastlyExecutedRequest,nextRequest);
                setNumberOfMoves += MovesCalculator.countSetMoves(lastlyExecutedRequest,nextRequest);
            }
            else {
                cylinderNumberOfMoves += nextRequest.getCylinderID();
                platterNumberOfMoves += nextRequest.getPlatterID();
                setNumberOfMoves += nextRequest.getSetID();
            }

            nextRequest.setWaitingTime(time-nextRequest.getMomentOfNotification());
            time += requestProcessingTime;

            lastlyExecutedRequest = nextRequest;
            listOfDeadRequests.add(nextRequest);

            nextRequest = findNextRequest();
        }
    }

    private Request findNextRequest () {
        if (queueOfRequests.size() == 0)
            return null;
        return queueOfRequests.remove(0);
    }
}