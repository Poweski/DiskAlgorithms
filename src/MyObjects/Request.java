package MyObjects;

public class Request {
    private final int cylinderID;
    private final int setID;
    private final int platterID;
    private final int momentOfNotification;
    private final double deadline;
    private int waitingTime;

    public Request(int cylinderID, int setID, int platterID, int momentOfNotification, double deadline) {
        this.cylinderID = cylinderID;
        this.setID = setID;
        this.platterID = platterID;
        this.momentOfNotification = momentOfNotification;
        this.deadline = deadline;
        this.waitingTime = 0;
    }

    public Request(Request request) {
        this.cylinderID = request.getCylinderID();
        this.setID = request.getSetID();
        this.platterID = request.getPlatterID();
        this.momentOfNotification = request.getMomentOfNotification();
        this.deadline = request.getDeadline();
        this.waitingTime = request.getWaitingTime();
    }


    public int getCylinderID() {
        return cylinderID;
    }

    public int getSetID() {
        return setID;
    }

    public int getPlatterID() {
        return platterID;
    }

    public int getMomentOfNotification() {
        return momentOfNotification;
    }

    public double getDeadline() {
        return deadline;
    }

    public int getWaitingTime() {
        return waitingTime;
    }

    public void setWaitingTime(int value) {
        this.waitingTime = value;
    }

    public Request getClone() {
        return new Request(this);
    }
}