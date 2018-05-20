package main.java.csvFilter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

import main.java.dataObjects.WifiSample;


public class LocationFilterByRadius implements SampleFilter, Serializable{
	private double radius;
	private double latitude;
	private double longtitude;

	public LocationFilterByRadius(double latitude, double longtitude, double radius) {
		this.latitude = latitude;
		this.longtitude = longtitude;
		this.radius = radius;
	}
	public String getOperator(){
		return "";
	}

	public double getLatitude() {
		return latitude;
	}

	public double getLongtitude() {
		return longtitude;
	}

	public double getRadius() {
		return radius;
	}

	@Override
	public ArrayList<WifiSample> filter(ArrayList<WifiSample> wifiSampleList) {
		return (ArrayList<WifiSample>) wifiSampleList.stream()
				.filter(wifiSample -> {
					return (Math.abs(wifiSample.getLatitude() - latitude) <= radius)
					&& (Math.abs(wifiSample.getLongitude() - longtitude) <= radius);
				}
						).collect(Collectors.toList());
	}
	
	
}
