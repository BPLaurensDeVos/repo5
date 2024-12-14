package com.example.repo5;

import com.example.repo4.Aggregator;
import com.example.repo1.Utility;

public class FinalProcessor {
    public String execute() {
        Aggregator aggregator = new Aggregator();
        return aggregator.aggregate() + " Finalized in Repo5.";
    }
}
