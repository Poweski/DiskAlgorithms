package Helpful;

import MyObjects.Disc;
import MyObjects.Request;

import java.util.Random;

public class RequestGenerator {
    public static void generateRequests
            (Disc disc, boolean CONCENTRATED_ON_ONE_SIDE, boolean SIMULTANEOUS_REQUESTS, boolean DEADLINE, int PERCENTAGE_DEADLINE_CHANCE, int NUMBER_OF_REQUESTS,
             int NUMBER_OF_PLATTERS, int SETS_PER_CYLINDER, int CYLINDERS_PER_PLATTER, int REQUEST_PROCESSING_TIME) {

        Random rng = new Random();

        int discLength = NUMBER_OF_PLATTERS * SETS_PER_CYLINDER * CYLINDERS_PER_PLATTER;
        int address = rng.nextInt(discLength-1);

        for (int requestID = 0; requestID < NUMBER_OF_REQUESTS; requestID++) {

            if (CONCENTRATED_ON_ONE_SIDE) {
                int flag = rng.nextInt(4);
                if (flag % 3 == 0)
                    while (disc.getRequest(address) != null)
                            address = rng.nextInt(discLength*3/4);
                else
                    while (disc.getRequest(address) != null)
                        address = rng.nextInt(discLength/4) + discLength*3/4;
            }
            else
                while (disc.getRequest(address) != null)
                    address = rng.nextInt(discLength-1);

            int momentOfNotification;
            if (SIMULTANEOUS_REQUESTS)
                momentOfNotification = 0;
            else
                momentOfNotification = rng.nextInt(discLength*REQUEST_PROCESSING_TIME*2/3);

            double deadline;
            if (DEADLINE) {
                if (rng.nextInt(99) < PERCENTAGE_DEADLINE_CHANCE)
                    deadline = momentOfNotification + rng.nextInt(discLength/5) + 15000;
                else
                    deadline = Double.POSITIVE_INFINITY;
            } else
                deadline = Double.POSITIVE_INFINITY;

            int[] position = disc.getCylinderBlockPlatterNumber(address);
            Request newRequest = new Request(position[0], position[1], position[2], momentOfNotification, deadline);
            disc.addRequest(address, newRequest);
        }
    }
}
