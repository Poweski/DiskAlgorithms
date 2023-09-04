import Algorithms.*;
import Helpful.RequestGenerator;
import MyObjects.*;

public class Main {
    private static final int REQUEST_PROCESSING_TIME = 1;
    private static final int PLATTER_CHANGE_TIME = 1;
    private static final int SET_CHANGE_TIME = 3;
    private static final int CYLINDER_CHANGE_TIME = 5;
    private static final int SETS_PER_CYLINDER = 150;
    private static final int CYLINDERS_PER_PLATTER = 100;
    private static final int NUMBER_OF_PLATTERS = 5;
    private static final int NUMBER_OF_REQUESTS = 1500;
    private static final boolean SIMULTANEOUS_REQUESTS = false;
    private static final boolean CONCENTRATED_ON_ONE_SIDE = true;
    private static final boolean DEADLINE = true;
    private static final int PERCENTAGE_DEADLINE_CHANCE = 5;


    public static void main(String[] args) {

        Disc disc = new Disc(new Request[CYLINDERS_PER_PLATTER][SETS_PER_CYLINDER][NUMBER_OF_PLATTERS]);

        RequestGenerator.generateRequests(disc, CONCENTRATED_ON_ONE_SIDE, SIMULTANEOUS_REQUESTS, DEADLINE, PERCENTAGE_DEADLINE_CHANCE,
                NUMBER_OF_REQUESTS, NUMBER_OF_PLATTERS, SETS_PER_CYLINDER, CYLINDERS_PER_PLATTER, REQUEST_PROCESSING_TIME);

        new FCFS(disc, CYLINDER_CHANGE_TIME, SET_CHANGE_TIME, PLATTER_CHANGE_TIME, REQUEST_PROCESSING_TIME);
        new SSTF(disc, CYLINDER_CHANGE_TIME, SET_CHANGE_TIME, PLATTER_CHANGE_TIME, REQUEST_PROCESSING_TIME);
        new SCAN(disc, CYLINDER_CHANGE_TIME, SET_CHANGE_TIME, PLATTER_CHANGE_TIME, REQUEST_PROCESSING_TIME);
        new C_SCAN(disc, CYLINDER_CHANGE_TIME, SET_CHANGE_TIME, PLATTER_CHANGE_TIME, REQUEST_PROCESSING_TIME);
        new EDF(disc, CYLINDER_CHANGE_TIME, SET_CHANGE_TIME, PLATTER_CHANGE_TIME, REQUEST_PROCESSING_TIME);
        new FD_SCAN(disc, CYLINDER_CHANGE_TIME, SET_CHANGE_TIME, PLATTER_CHANGE_TIME, REQUEST_PROCESSING_TIME);
    }
}