package com.example.genie.domain.pot.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class OTT {
    int ottNumber;
    String ottName;

    public OTT(int ottNumber, String ottName) {
        this.ottNumber = ottNumber;
        this.ottName = ottName;
    }

    public static List<OTT> getOTTList(){
        List<OTT> ottList = new ArrayList<>();
        ottList.add(new OTT(1, "NetFlix"));
        ottList.add(new OTT(2, "DisenyPlus"));
        ottList.add(new OTT(3, "Watcha"));
        ottList.add(new OTT(4, "Wavve"));
        return ottList;
    }
}
