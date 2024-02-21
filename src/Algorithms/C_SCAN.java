package Algorithms;

import MyObjects.*;
import Helpful.*;

import java.util.ArrayList;

public class C_SCAN {
    private final Disc disc;
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

    public C_SCAN(Disc disc, int cylinderChangeTime, int setChangeTime, int platterChangeTime, int requestProcessingTime) {

        this.cylinderChangeTime = cylinderChangeTime;
        this.setChangeTime = setChangeTime;
        this.platterChangeTime = platterChangeTime;
        this.requestProcessingTime = requestProcessingTime;
        this.disc = disc.getSelfClone();

        runTheSimulation();

        ResultManager.presentResults("C-SCAN", listOfDeadRequests, time, cylinderNumberOfMoves,
                setNumberOfMoves, platterNumberOfMoves);
    }

    private void runTheSimulation() {

        Request nextRequest = findNextRequest();

        while (nextRequest != null) {

            if (time < nextRequest.getMomentOfNotification()) {
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

        int tempTime = time;
        int previousAddress = disc.getAddress(lastlyExecutedRequest);

        if (previousAddress == -1)
            previousAddress = 0;

        int lastServicedRequestAddress = previousAddress;
        int potentialAddress = previousAddress;
        int numberOfChecksForTheSameRequest = 0;
        boolean isAnyAlive = false;
        Request potentialRequest;

        while (isAnyAlive || !(numberOfChecksForTheSameRequest == 2)) {

            potentialAddress += 1;

            if (potentialAddress > disc.getLastAddress()) {
                potentialAddress = 0;
            }

            if (lastServicedRequestAddress == potentialAddress) {
                numberOfChecksForTheSameRequest++;
            }

            potentialRequest = disc.getRequest(potentialAddress);
            tempTime += DistanceCalculator.getDifferenceInTimeBetweenTwoSegments(previousAddress, potentialAddress,
                    disc, platterChangeTime, cylinderChangeTime, setChangeTime);

            if (potentialRequest != null) {
                isAnyAlive = true;
                if (potentialRequest.getMomentOfNotification() <= tempTime) {
                    return disc.removeRequest(potentialAddress);
                }
            }

            previousAddress = potentialAddress;
        }
        return null;
    }
}