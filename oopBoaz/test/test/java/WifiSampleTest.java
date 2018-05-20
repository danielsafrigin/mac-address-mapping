package test.java;


import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import main.java.dataObjects.WifiSample;
import main.java.dataObjects.WifiSpot;

public class WifiSampleTest {
	WifiSample sample;

	@Before
	public void setUp() throws Exception {
		sample = new WifiSample("12/05/17 11:33", "model=SM-G950F_device=dreamlte", 32, 35, 600, "");
	}

	@Test
	public void testAddSpot() {

		WifiSpot spot = new WifiSpot("Home", "34:8f:27:20:89:b8", 1, -90);
		sample.addSpot(spot);
		assertTrue(sample.getWifiList() != null && sample.getWifiList().size() == 1
				&& sample.getWifiList().get(0).getSignal() == -90 && sample.getWifiList().get(0).getFrequncy() == 1
				&& sample.getWifiList().get(0).getSsid().equals("Home")
				&& sample.getWifiList().get(0).getMac().equals("34:8f:27:20:89:b8"));
	}

	@Test
	public void testSetLatitude() {
		sample.setLatitude(20);
		double expected = 20;
		double actual = sample.getLatitude();
		assertEquals(actual, expected, 0);
	}

	@Test
	public void testSetLongitude() {
		sample.setLongitude(18);
		double expected = 18;
		double actual = sample.getLongitude();
		assertEquals(actual, expected, 0);
	}

	@Test
	public void testSetAltitude() {
		sample.setAltitude(15);
		double expected = 15;
		double actual = sample.getAltitude();
		assertEquals(actual, expected, 0);
	}

	@Test
	public void testGetTimeStamp() {
		String expected = "12/05/17 11:33";
		String actual = sample.getTimeStamp();
		assertEquals(actual, expected);
	}

	@Test
	public void testGetPhoneId() {
		String expected = "model=SM-G950F_device=dreamlte";
		String actual = sample.getPhoneId();
		assertEquals(actual, expected);
	}

	@Test
	public void testGetLatitude() {
		double expected = 32;
		double actual = sample.getLatitude();
		assertEquals(actual, expected, 0);
	}

	@Test
	public void testGetLongitude() {
		double expected = 35;
		double actual = sample.getLongitude();
		assertEquals(actual, expected, 0);
	}

	@Test
	public void testGetAltitude() {
		double expected = 600;
		double actual = sample.getAltitude();
		assertEquals(actual, expected, 0);
	}

	@Test
	public void testGetWifiNames() {
		String expected = "";
		String actual = sample.getWifiNames();
		assertEquals(actual, expected);
	}

	@Test
	public void testGetWifiList() {
		assertTrue(sample.getWifiList().size()==0);
	}

	@Test
	public void testToString() {
		String expected = "WifiSample [timeStamp=12/05/17 11:33, phoneId=model=SM-G950F_device=dreamlte, latitude=32.0, longitude=35.0, altitude=600.0, wifiNames=, wifiList=[]]";
		String actual = sample.toString();
		assertEquals(actual, expected);
	}

}
