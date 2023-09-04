package Helpful;

import MyObjects.Request;

import java.util.ArrayList;

public class TableManager {

    @SuppressWarnings("unchecked")
    public static ArrayList<Request> convertRequestTableToArrayList(Request[][][] tab) {

        ArrayList<Request> resultArray = new ArrayList<>();

        for (Request[][] cylinder : tab)
            for (Request[] block : cylinder)
                for (Request platter : block)
                    if (platter != null)
                        resultArray.add(new Request(platter));

        return resultArray;
    }
}