package main.java.csvFilter;

import java.util.ArrayList;

import main.java.dataObjects.WifiSample;

public interface SampleFilter {
	ArrayList<WifiSample> filter(ArrayList<WifiSample> wifiSampleList);
	String getOperator();
	String toString();
}
