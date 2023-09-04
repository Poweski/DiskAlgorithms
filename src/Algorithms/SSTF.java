package Algorithms;

import Comparators.*;
import MyObjects.*;
import Helpful.*;

import java.util.ArrayList;

public class SSTF {
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

    public SSTF (Disc disc, int cylinderChangeTime, int setChangeTime, int platterChangeTime, int requestProcessingTime) {

        this.cylinderChangeTime = cylinderChangeTime;
        this.setChangeTime = setChangeTime;
        this.platterChangeTime = platterChangeTime;
        this.requestProcessingTime = requestProcessingTime;

        queueOfRequests = TableManager.convertRequestTableToArrayList(disc.getDisc());
        queueOfRequests.sort(new SortByMomentOfNotification());

        runTheSimulation();

        ResultManager.presentResults("SSTF", listOfDeadRequests, time, cylinderNumberOfMoves,
                setNumberOfMoves, platterNumberOfMoves);
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

        if (lastlyExecutedRequest == null || queueOfRequests.size() == 1)
            return queueOfRequests.remove(0);

        Request nearestRequest = queueOfRequests.get(0);
        int bestDifferenceInTime = DistanceCalculator.getDifferenceInTimeBetweenTwoRequests
                (lastlyExecutedRequest,nearestRequest,platterChangeTime,cylinderChangeTime, setChangeTime);

        int numberOfPotentialRequests = 1;

        while (numberOfPotentialRequests < queueOfRequests.size() &&
                queueOfRequests.get(numberOfPotentialRequests).getMomentOfNotification() <=
                        Math.max(nearestRequest.getMomentOfNotification(), time)) {

            Request potentialRequest = queueOfRequests.get(numberOfPotentialRequests);
            int potentialDifferenceInTime = DistanceCalculator.getDifferenceInTimeBetweenTwoRequests
                    (lastlyExecutedRequest,potentialRequest,platterChangeTime,cylinderChangeTime, setChangeTime);

            if (potentialDifferenceInTime < bestDifferenceInTime) {
                nearestRequest = potentialRequest;
                bestDifferenceInTime = potentialDifferenceInTime;
            }

            numberOfPotentialRequests++;
        }
        queueOfRequests.remove(nearestRequest);
        return nearestRequest;
    }
}