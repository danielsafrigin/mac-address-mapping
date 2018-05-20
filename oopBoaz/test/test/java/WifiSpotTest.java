package test.java;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.dataObjects.WifiSpot;

public class WifiSpotTest {

	WifiSpot spot;

	@Before
	public void setUp() throws Exception {
		spot = new WifiSpot("Home", "34:8f:27:20:89:b8", 1, -90);
	}

	@Test
	public void testToString() {
		String expected = "WifiSpot [ssid=" + "Home" + ", mac=" + "34:8f:27:20:89:b8" + ", frequncy=" + 1 + ", signal="
				+ -90 + "]";
		String actual = spot.toString();
		assertEquals(actual, expected);
	}

	@Test
	public void testGetSsid() {
		String expected = "Home";
		String actual = spot.getSsid();
		assertEquals(actual, expected);
	}

	@Test
	public void testGetMac() {
		String expected = "34:8f:27:20:89:b8";
		String actual = spot.getMac();
		assertEquals(actual, expected);
	}

	@Test
	public void testGetFrequncy() {
		int expected = 1;
		int actual = spot.getFrequncy();
		assertEquals(actual, expected);
	}

	@Test
	public void testGetSignal() {
		int expected = -90;
		int actual = spot.getSignal();
		assertEquals(actual, expected);
	}

}
