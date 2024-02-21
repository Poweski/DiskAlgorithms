package Algorithms;

import Comparators.*;
import MyObjects.*;
import Helpful.*;

import java.util.ArrayList;

public class EDF {
    private final ArrayList<Request> queueOfRequests;
    private final ArrayList<Request> listOfDeadRequests = new ArrayList<>();
    private Request lastlyExecutedRequest = null;
    private final int requestProcessingTime;
    private final int cylinderChangeTime;
    private final int setChangeTime;
    private final int platterChangeTime;
    private int cylinderNumberOfMoves = 0;
    private int platterNumberOfMoves = 0;
    private int setNumberOfMoves = 0;
    private int time = 0;

    public EDF (Disc disc, int cylinderChangeTime, int setChangeTime, int platterChangeTime, int requestProcessingTime) {

        this.setChangeTime = setChangeTime;
        this.cylinderChangeTime = cylinderChangeTime;
        this.platterChangeTime = platterChangeTime;
        this.requestProcessingTime = requestProcessingTime;

        queueOfRequests = TableManager.convertRequestTableToArrayList(disc.getDisc());
        queueOfRequests.sort(new SortByMomentOfNotification());

        runTheSimulation();

        ResultManager.presentResults("EDF", listOfDeadRequests, time, cylinderNumberOfMoves,
                setNumberOfMoves, platterNumberOfMoves);
    }

    private void runTheSimulation() {

        Request nextRequest = findNextRequest();

        while (nextRequest != null) {

            if (nextRequest.getMomentOfNotification() > time) {
                time = nextRequest.getMomentOfNotification();
            }

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

        if (queueOfRequests.isEmpty())
            return null;

        if (lastlyExecutedRequest == null || queueOfRequests.size() == 1) {
            return queueOfRequests.removeFirst();
        }

        ArrayList<Request> consideredRequests = new ArrayList<>();
        consideredRequests.add(queueOfRequests.getFirst());

        int consideredSize = 1;

        while (consideredSize < queueOfRequests.size() && queueOfRequests.get(consideredSize).getMomentOfNotification()
                <= Math.max(consideredRequests.getFirst().getMomentOfNotification(), time)) {
            consideredRequests.add(queueOfRequests.get(consideredSize));
            consideredSize++;
        }

        consideredRequests.sort(new SortByDeadline());

        for (Request request : consideredRequests) {

            int timeAfterArrivalToRequest = time + DistanceCalculator.getDifferenceInTimeBetweenTwoRequests
                    (lastlyExecutedRequest, request, platterChangeTime, cylinderChangeTime, setChangeTime);

            if (request.getDeadline() == Double.POSITIVE_INFINITY) {
                return queueOfRequests.removeFirst();
            }

            if (timeAfterArrivalToRequest <= request.getDeadline()) {
                queueOfRequests.remove(request);
                return request;
            }
        }
        return queueOfRequests.removeFirst();
    }
}