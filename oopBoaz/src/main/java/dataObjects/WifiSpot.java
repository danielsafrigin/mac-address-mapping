package main.java.dataObjects;

/**
 * 
 * this class represents a Single WIFI spot of WifiSample, each WIFI spot
 * contains SSID, MAC, frequency and signal.
 * 
 * @author daniel
 */

public class WifiSpot {
	private String ssid;
	private String mac;
	private int frequncy;
	private int signal;

	public WifiSpot(String ssid, String mac, int frequncy, int singal) {
		this.ssid = ssid;
		this.mac = mac;
		this.frequncy = frequncy;
		this.signal = singal;
	}
	
	
	@Override
	public String toString() {
		return "WifiSpot [ssid=" + ssid + ", mac=" + mac + ", frequncy=" + frequncy + ", signal=" + signal + "]";
	}

	/**
	 * 
	 * @return ssid
	 */
	public String getSsid() {
		return ssid;
	}

	/**
	 * 
	 * @return mac
	 */
	public String getMac() {
		return mac;
	}

	/**
	 * 
	 * @return frequncy
	 */
	public int getFrequncy() {
		return frequncy;
	}
	
	/**
	 * 
	 * @return signal
	 */
	public int getSignal() {
		return signal;
	}

}
