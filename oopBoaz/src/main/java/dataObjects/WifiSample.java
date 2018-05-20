package main.java.dataObjects;

import java.util.ArrayList;

/**
 * 
 * this class represents the row object, each row object is a line of a CSV
 * file.
 * 
 * @author daniel
 */

public class WifiSample {
	private String timeStamp;
	private String phoneId;
	private double latitude;
	private double longitude;
	private double altitude;
	private String wifiNames;
	private ArrayList<WifiSpot> wifiList;

	// CONSTRACTORS
	public WifiSample(String[] row) {
		this.timeStamp = row[0];
		this.phoneId = row[1];
		this.latitude = Double.parseDouble(row[2]);
		this.longitude = Double.parseDouble(row[3]);
		this.altitude = Double.parseDouble(row[4]);
		this.wifiNames = row[5];
		this.wifiList = makeWifiListFromRow(row);
	}

	public WifiSample(WifiSample sample, int SpotIndex) {
		this.timeStamp = sample.getTimeStamp();
		this.phoneId = sample.getPhoneId();
		this.latitude = sample.getLatitude();
		this.longitude = sample.getLongitude();
		this.altitude = sample.getAltitude();
		this.wifiNames = sample.getWifiList().get(SpotIndex).getSsid();
		this.wifiList = onePointFromSample(sample.getWifiList().get(SpotIndex));
	}

	public WifiSample(String mac1, String mac2, String mac3, String signal1, String signal2, String signal3) {
		this.timeStamp = "";
		this.phoneId = "";
		this.latitude = 1;
		this.longitude = 1;
		this.altitude = 1;
		this.wifiNames = "";
		this.wifiList = signalsToWifiSpotList(mac1, mac2, mac3, signal1, signal2, signal3);
	}

	public WifiSample(WifiSample sample) {
		this.timeStamp = sample.getTimeStamp();
		this.phoneId = sample.getPhoneId();
		this.latitude = sample.getLatitude();
		this.longitude = sample.getLongitude();
		this.altitude = sample.getAltitude();
		this.wifiNames = sample.getWifiNames();
		this.wifiList = allPointsFromSample(sample.getWifiList());
	}

	public WifiSample(String timeStamp, String phoneId, double latitude, double longitude, double altitude,
			String wifiNames) {
		this.timeStamp = timeStamp;
		this.phoneId = phoneId;
		this.latitude = latitude;
		this.longitude = longitude;
		this.altitude = altitude;
		this.wifiNames = wifiNames;
		wifiList = new ArrayList<WifiSpot>();
	}

	
	// PRIVATE FUNCTIONS
	/**
	 * 
	 * @param mac1
	 * @param mac2
	 * @param mac3
	 * @param signal1
	 * @param signal2
	 * @param signal3
	 * @return
	 */
	private ArrayList<WifiSpot> signalsToWifiSpotList(String mac1, String mac2, String mac3, String signal1,
			String signal2, String signal3) {
		WifiSpot wifiSpot1 = new WifiSpot("", mac1, -1, Integer.parseInt(signal1));
		WifiSpot wifiSpot2 = new WifiSpot("", mac2, -1, Integer.parseInt(signal2));
		WifiSpot wifiSpot3 = new WifiSpot("", mac3, -1, Integer.parseInt(signal3));
		ArrayList<WifiSpot> wifiSpotList = new ArrayList<WifiSpot>();
		wifiSpotList.add(wifiSpot1);
		wifiSpotList.add(wifiSpot2);
		wifiSpotList.add(wifiSpot3);
		return wifiSpotList;
	}

	/**
	 * 
	 * @param wifiSpotList
	 * @return
	 */
	private ArrayList<WifiSpot> allPointsFromSample(ArrayList<WifiSpot> wifiSpotList) {
		ArrayList<WifiSpot> list = new ArrayList<WifiSpot>();
		list.addAll(wifiSpotList);
		return list;
	}

	/**
	 * 
	 * @param wifiSpot
	 * @return
	 */
	private ArrayList<WifiSpot> onePointFromSample(WifiSpot wifiSpot) {
		ArrayList<WifiSpot> wifiSpotList = new ArrayList<WifiSpot>();
		wifiSpotList.add(wifiSpot);
		return wifiSpotList;
	}

	/**
	 * 
	 * @param row
	 * @return
	 */
	private ArrayList<WifiSpot> makeWifiListFromRow(String[] row) {
		int counter = (row.length - 6) / 4;

		ArrayList<WifiSpot> list = new ArrayList<WifiSpot>();
		for (int i = 0; i < counter; i++) {

			WifiSpot wifi = new WifiSpot(row[i * 4 + 6], row[i * 4 + 7], Integer.parseInt(row[i * 4 + 8]),
					(int) Double.parseDouble(row[i * 4 + 9]));
			list.add(wifi);
		}
		return list;
	}

	
	// PUBLIC FUNCTION	
	/**
	 * Add a new spot to WIFI spot list
	 * @param wifiSpot to be added
	 */
	public void addSpot(WifiSpot wifiSpot) {
		this.wifiList.add(wifiSpot);
	}

	@Override
	public String toString() {
		return "WifiSample [timeStamp=" + timeStamp + ", phoneId=" + phoneId + ", latitude=" + latitude + ", longitude="
				+ longitude + ", altitude=" + altitude + ", wifiNames=" + wifiNames + ", wifiList="
				+ wifiList.toString() + "]";
	}

	
	// GETTERS
	/**
	 * 
	 * @return time stamp of the sample
	 */
	public String getTimeStamp() {
		return timeStamp;
	}

	/**
	 * 
	 * @return phone id of the sample
	 */
	public String getPhoneId() {
		return phoneId;
	}

	/**
	 * 
	 * @return latitude of the sample
	 */
	public double getLatitude() {
		return latitude;
	}

	/**
	 * 
	 * @return longitude of the sample
	 */
	public double getLongitude() {
		return longitude;
	}

	/**
	 * 
	 * @return altitude of the sample
	 */
	public double getAltitude() {
		return altitude;
	}

	/**
	 * 
	 * @return WIFI names of the sample
	 */
	public String getWifiNames() {
		return wifiNames;
	}

	/**
	 * 
	 * @return list of all WIFI spots of the sample
	 */
	public ArrayList<WifiSpot> getWifiList() {
		return wifiList;
	}

	
	// SETTERS
	/**
	 * 
	 * @param latitude to be set
	 */
	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	/**
	 * 
	 * @param longitude to be set
	 */
	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	/**
	 * 
	 * @param altitude to be set
	 */
	public void setAltitude(double altitude) {
		this.altitude = altitude;
	}

	
}
