package MyObjects;

public class Disc {

    private final Request[][][] disc;

    public Disc (Request[][][] requests) {
        this.disc = requests;
    }

    public Request[][][] getDisc() {
        return disc;
    }

    public int getCylindersPerPlatterNumber() {
        return disc.length;
    }

    public int getBlocksPerCylinderNumber() {
        return disc[0].length;
    }

    public int getPlattersNumber() {
        return disc[0][0].length;
    }

    public int[] getCylinderBlockPlatterNumber(int address) {

        int cylinder = (address / (getPlattersNumber() * getBlocksPerCylinderNumber()));
        int block = ((address % (getPlattersNumber() * getBlocksPerCylinderNumber())) / getPlattersNumber());
        int platter = (address % getPlattersNumber());

        return new int[]{cylinder, block, platter};
    }

    public Request getRequest (int address) {
        int[] position = getCylinderBlockPlatterNumber(address);
        return disc[position[0]][position[1]][position[2]];
    }

    public int getAddress (int cylinder, int block, int platter) {
        return cylinder * getBlocksPerCylinderNumber() * getPlattersNumber()
                + block * getPlattersNumber()
                + platter;
    }

    public int getAddress (Request request) {
        if (request == null) {
            return -1;
        }
        return getAddress(request.getCylinderID(), request.getSetID(), request.getPlatterID());
    }

    public int getLastAddress () {
        return getCylindersPerPlatterNumber() * getBlocksPerCylinderNumber() * getPlattersNumber() - 1;
    }

    public void addRequest(int address, Request request) {
        int[] position = getCylinderBlockPlatterNumber(address);
        disc[position[0]][position[1]][position[2]] = request;
    }

    public Request removeRequest(int address) {
        Request temp = getRequest(address);
        int[] position = getCylinderBlockPlatterNumber(address);
        disc[position[0]][position[1]][position[2]] = null;
        return temp;
    }

    public Disc getSelfClone() {
        Request[][][] newReqTab = new Request[disc.length][disc[0].length][disc[0][0].length];
        for (int cID = 0; cID < disc.length; cID++) {
            for (int sID = 0; sID < disc[0].length; sID++) {
                for (int pID = 0; pID < disc[0][0].length; pID++) {
                    if (disc[cID][sID][pID] != null) {
                        newReqTab[cID][sID][pID] = disc[cID][sID][pID].getClone();
                    }
                }
            }
        }

        return new Disc(newReqTab);
    }
}