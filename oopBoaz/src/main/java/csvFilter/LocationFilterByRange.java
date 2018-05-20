package main.java.csvFilter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

import main.java.dataObjects.WifiSample;

public class LocationFilterByRange implements SampleFilter, Serializable {
	private String operator;
	private double startLatitude;
	private double startLongtitude;
	private double startAltitude;
	private double endLatitude;
	private double endLongtitude;
	private double endAltitude;

	public LocationFilterByRange(String startLatitude, String startLongtitude, String startAltitude, String endLatitude,
			String endLongtitude, String endAltitude, String operator) {
		this.operator = operator;
		this.startLatitude = Double.parseDouble(startLatitude);
		this.startLongtitude = Double.parseDouble(startLongtitude);
		this.startAltitude = Double.parseDouble(startAltitude);
		this.endLatitude = Double.parseDouble(endLatitude);
		this.endLongtitude = Double.parseDouble(endLongtitude);
		this.endAltitude = Double.parseDouble(endAltitude);
	}

	public String getOperator() {
		return operator;
	}

	public double getStartLatitude() {
		return startLatitude;
	}

	public double getStartLongtitude() {
		return startLongtitude;
	}

	public double getStartAltitude() {
		return startAltitude;
	}

	public double getEndLatitude() {
		return endLatitude;
	}

	public double getEndLongtitude() {
		return endLongtitude;
	}

	public double getEndAltitude() {
		return endAltitude;
	}

	@Override
	public String toString() {
		String s = "Location(" + startLatitude + "<Lat<" + endLatitude + " " + startLongtitude + "<Lon<" + endLongtitude
				+ " " + startAltitude + "<Alt<" + endAltitude + ")";
		if (operator.contains("not"))
			s = "!" + s;
		if (operator.contains("and"))
			s = "& " + s;
		else {
			s = "| " + s;
		}

		return s;
	}

	@Override
	public ArrayList<WifiSample> filter(ArrayList<WifiSample> wifiSampleList) {
		return (ArrayList<WifiSample>) wifiSampleList.stream().filter(wifiSample -> {
			return (wifiSample.getLatitude() - startLatitude >= 0) && (wifiSample.getLatitude() - endLatitude <= 0)
					&& (wifiSample.getLongitude() - startLongtitude >= 0)
					&& (wifiSample.getLongitude() - endLongtitude <= 0)
					&& (wifiSample.getAltitude() - startAltitude >= 0) && (wifiSample.getAltitude() - endAltitude <= 0);
		}).collect(Collectors.toList());
	}

}