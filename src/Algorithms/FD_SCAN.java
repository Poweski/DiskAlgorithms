package Algorithms;

import Comparators.*;
import MyObjects.*;
import Helpful.*;

import java.util.ArrayList;

public class FD_SCAN {
    private final Disc disc;
    private final ArrayList<Request> listOfDeadRequests = new ArrayList<>();
    private final ArrayList<Request> queueOfRequests;
    private Request lastlyExecutedRequest = null;
    private final int requestProcessingTime;
    private final int cylinderChangeTime;
    private final int setChangeTime;
    private final int platterChangeTime;
    private int cylinderNumberOfMoves = 0;
    private int platterNumberOfMoves = 0;
    private int setNumberOfMoves = 0;
    private int time = 0;


    public FD_SCAN (Disc disc, int cylinderChangeTime, int setChangeTime, int platterChangeTime, int requestProcessingTime) {

        this.disc = disc.getSelfClone();
        this.setChangeTime = setChangeTime;
        this.cylinderChangeTime = cylinderChangeTime;
        this.platterChangeTime = platterChangeTime;
        this.requestProcessingTime = requestProcessingTime;

        queueOfRequests = TableManager.convertRequestTableToArrayList(disc.getDisc());
        queueOfRequests.sort(new SortByMomentOfNotification());

        runTheSimulation();

        ResultManager.presentResults("FD-SCAN", listOfDeadRequests, time, cylinderNumberOfMoves,
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

    private Request findNextRequest() {

        if (queueOfRequests.isEmpty()) {
            return null;
        }
        if (lastlyExecutedRequest == null) {
            return disc.removeRequest(disc.getAddress(queueOfRequests.removeFirst()));
        }

        ArrayList<Request> consideredRequests = new ArrayList<>();
        consideredRequests.add(queueOfRequests.getFirst());

        int consideredSize = 1;
        int maxNotificationTime = Math.max(consideredRequests.getFirst().getMomentOfNotification(), time);

        while (consideredSize < queueOfRequests.size() &&
                queueOfRequests.get(consideredSize).getMomentOfNotification() <= maxNotificationTime) {
            consideredRequests.add(queueOfRequests.get(consideredSize));
            consideredSize++;
        }

        consideredRequests.sort(new SortByDeadline());

        for (Request request: consideredRequests) {

            int timeAfterArrivalToRequest = time + DistanceCalculator.getDifferenceInTimeBetweenTwoRequests
                    (lastlyExecutedRequest, request, platterChangeTime, cylinderChangeTime, setChangeTime);

            if (request.getDeadline() == Double.POSITIVE_INFINITY) {
                return queueOfRequests.removeFirst();
            }

            if (timeAfterArrivalToRequest <= request.getDeadline()) {

                int actualAddress = disc.getAddress(lastlyExecutedRequest);
                int change = (++actualAddress < disc.getAddress(request)) ? 1 : -1;

                while (true) {
                    if (disc.getRequest(actualAddress) != null) {
                        if (disc.getRequest(actualAddress).getMomentOfNotification() <=
                                (time + DistanceCalculator.getDifferenceInTimeBetweenTwoSegments
                                        (disc.getAddress(lastlyExecutedRequest), actualAddress, disc,
                                                platterChangeTime, cylinderChangeTime, setChangeTime))) {
                            break;
                        }
                    }

                    actualAddress += change;

                    if (actualAddress == disc.getAddress(request)) {
                        break;
                    }
                }

                for (int id = 0; id < queueOfRequests.size(); id++) {
                    if (disc.getAddress(queueOfRequests.get(id)) == actualAddress) {
                        queueOfRequests.remove(id);
                        break;
                    }
                }

                return disc.removeRequest(actualAddress);
            }
        }

        return queueOfRequests.removeFirst();
    }
}