package test.java;


import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.csvFilter.LocationFilterByRange;
import main.java.csvFilter.PhoneIdFilter;
import main.java.csvFilter.SampleFilter;
import main.java.dataObjects.DataBase;
import main.java.dataObjects.WifiSample;

public class DataBaseTest {
	DataBase dataBase;

	@Test
	public void testAddFilter() {
		dataBase = new DataBase();
		LocationFilterByRange filter = new LocationFilterByRange("32.1", "35.21", "688.0", "32.11", "35.22", "690.0",
				"or");
		dataBase.addFilter(filter);
		String expected = "| Location(32.1<Lat<32.11 35.21<Lon<35.22 688.0<Alt<690.0)";
		String actual = dataBase.getFilterList().get(0).toString();
		assertEquals(actual, expected);
	}

	@Test
	public void testAdd() {
		dataBase = new DataBase();
		String[] s = ("2017-12-03 08:46:58,model=Lenovo PB2-690Y_device=PB2PRO," + "32.00389461,35.11035436,695.0"
				+ ",1,HelpDesk,60:e3:27:7b:e0:98,4,-87.0").split(",");
		WifiSample t = new WifiSample(s);
		dataBase.add(t);
		String expected = "WifiSample [timeStamp=2017-12-03 08:46:58, phoneId=model=Lenovo PB2-690Y_device=PB2PRO, "
				+ "latitude=32.00389461, longitude=35.11035436, altitude=695.0, wifiNames=1, "
				+ "wifiList=[WifiSpot [ssid=HelpDesk, mac=60:e3:27:7b:e0:98, frequncy=4, signal=-87]]]";
		String actual = dataBase.getWifiSampleList().get(0).toString();
		assertTrue(dataBase.size() == 1 && expected.equals(actual));
	}

	@Test
	public void testAddAllArrayListOfWifiSample() {
		dataBase = new DataBase();
		String[] s = ("2017-12-03 08:46:58,model=Lenovo PB2-690Y_device=PB2PRO," + "32.00389461,35.11035436,695.0"
				+ ",1,HelpDesk,60:e3:27:7b:e0:98,4,-87.0").split(",");
		WifiSample t = new WifiSample(s);

		String[] s2 = ("2017-12-03 08:39:16,model=SM-G950F_device=dreamlte,"
				+ "32.106181954122206,35.21168763894033,689.1981253462078"
				+ ",2, ,00:1a:dd:f4:f5:c4,13,-87.0,Bezeq-NGN_E77EB9,cc:b2:55:e7:7e:ba,6,-87.0").split(",");
		WifiSample t2 = new WifiSample(s2);

		ArrayList<WifiSample> list = new ArrayList<WifiSample>();
		list.add(t);
		list.add(t2);
		dataBase.addAll(list);

		String expected = "WifiSample [timeStamp=2017-12-03 08:46:58, phoneId=model=Lenovo PB2-690Y_device=PB2PRO, "
				+ "latitude=32.00389461, longitude=35.11035436, altitude=695.0, wifiNames=1, "
				+ "wifiList=[WifiSpot [ssid=HelpDesk, mac=60:e3:27:7b:e0:98, frequncy=4, signal=-87]]]";

		String actual = dataBase.getWifiSampleList().get(0).toString();

		String expected2 = "WifiSample [timeStamp=2017-12-03 08:39:16, phoneId=model=SM-G950F_device=dreamlte, "
				+ "latitude=32.106181954122206, longitude=35.21168763894033, altitude=689.1981253462078, wifiNames=2, "
				+ "wifiList=[WifiSpot [ssid= , mac=00:1a:dd:f4:f5:c4, frequncy=13, signal=-87], "
				+ "WifiSpot [ssid=Bezeq-NGN_E77EB9, mac=cc:b2:55:e7:7e:ba, frequncy=6, signal=-87]]]";

		String actual2 = dataBase.getWifiSampleList().get(1).toString();
		assertTrue(dataBase.size() == 2 && expected.equals(actual) && expected2.equals(actual2));

	}

	@Test
	public void testAddAllDataBase() {
		dataBase = new DataBase();
		String[] s = ("2017-12-03 08:46:58,model=Lenovo PB2-690Y_device=PB2PRO," + "32.00389461,35.11035436,695.0"
				+ ",1,HelpDesk,60:e3:27:7b:e0:98,4,-87.0").split(",");
		WifiSample t = new WifiSample(s);

		String[] s2 = ("2017-12-03 08:39:16,model=SM-G950F_device=dreamlte,"
				+ "32.106181954122206,35.21168763894033,689.1981253462078"
				+ ",2, ,00:1a:dd:f4:f5:c4,13,-87.0,Bezeq-NGN_E77EB9,cc:b2:55:e7:7e:ba,6,-87.0").split(",");
		WifiSample t2 = new WifiSample(s2);

		ArrayList<WifiSample> list = new ArrayList<WifiSample>();
		list.add(t);
		list.add(t2);
		dataBase.addAll(list);

		DataBase dataBase2 = new DataBase();
		dataBase2.addAll(dataBase);

		String expected = "WifiSample [timeStamp=2017-12-03 08:46:58, phoneId=model=Lenovo PB2-690Y_device=PB2PRO, "
				+ "latitude=32.00389461, longitude=35.11035436, altitude=695.0, wifiNames=1, "
				+ "wifiList=[WifiSpot [ssid=HelpDesk, mac=60:e3:27:7b:e0:98, frequncy=4, signal=-87]]]";

		String actual = dataBase2.getWifiSampleList().get(0).toString();

		String expected2 = "WifiSample [timeStamp=2017-12-03 08:39:16, phoneId=model=SM-G950F_device=dreamlte, "
				+ "latitude=32.106181954122206, longitude=35.21168763894033, altitude=689.1981253462078, wifiNames=2, "
				+ "wifiList=[WifiSpot [ssid= , mac=00:1a:dd:f4:f5:c4, frequncy=13, signal=-87], "
				+ "WifiSpot [ssid=Bezeq-NGN_E77EB9, mac=cc:b2:55:e7:7e:ba, frequncy=6, signal=-87]]]";

		String actual2 = dataBase2.getWifiSampleList().get(1).toString();
		assertTrue(dataBase2.getWifiSampleList().size() == 2 && expected.equals(actual) && expected2.equals(actual2));
	}

	@Test
	public void testClear() {
		dataBase = new DataBase();
		String[] s = ("2017-12-03 08:46:58,model=Lenovo PB2-690Y_device=PB2PRO," + "32.00389461,35.11035436,695.0"
				+ ",1,HelpDesk,60:e3:27:7b:e0:98,4,-87.0").split(",");
		WifiSample t = new WifiSample(s);
		dataBase.add(t);
		dataBase.clear();
		assertTrue(dataBase.getWifiSampleList().size() == 0);
	}

	@Test
	public void testGetFilterList() {
		dataBase = new DataBase();
		LocationFilterByRange filter = new LocationFilterByRange("32.1", "35.21", "688.0", "32.11", "35.22", "690.0",
				"or");
		dataBase.addFilter(filter);
		String expected = "| Location(32.1<Lat<32.11 35.21<Lon<35.22 688.0<Alt<690.0)";
		String actual = dataBase.getFilterList().get(0).toString();
		assertEquals(actual, expected);
	}

	@Test
	public void testFilterListToString() {
		dataBase = new DataBase();
		LocationFilterByRange filter = new LocationFilterByRange("32.1", "35.21", "688.0", "32.11", "35.22", "690.0",
				"or");
		PhoneIdFilter filter2 = new PhoneIdFilter("Redmi 4A", "andnot");
		dataBase.addFilter(filter);
		dataBase.addFilter(filter2);
		String expected = "| Location(32.1<Lat<32.11 35.21<Lon<35.22 688.0<Alt<690.0)& !Device(Redmi 4A)";
		String actual = dataBase.filterListToString();

		assertEquals(actual, expected);

	}

	@Test
	public void testSize() {

		dataBase = new DataBase();
		String[] s = ("2017-12-03 08:46:58,model=Lenovo PB2-690Y_device=PB2PRO," + "32.00389461,35.11035436,695.0"
				+ ",1,HelpDesk,60:e3:27:7b:e0:98,4,-87.0").split(",");
		WifiSample t = new WifiSample(s);
		dataBase.add(t);
		int expected = 1;
		int actual = dataBase.size();
		assertEquals(actual, expected);
	}

	@Test
	public void testGetNumberOfMacs() {
		dataBase = new DataBase();
		String[] s = ("2017-12-03 08:46:58,model=Lenovo PB2-690Y_device=PB2PRO," + "32.00389461,35.11035436,695.0"
				+ ",1,HelpDesk,60:e3:27:7b:e0:98,4,-87.0").split(",");
		WifiSample t = new WifiSample(s);

		String[] s2 = ("2017-12-03 08:39:16,model=SM-G950F_device=dreamlte,"
				+ "32.106181954122206,35.21168763894033,689.1981253462078"
				+ ",2, ,00:1a:dd:f4:f5:c4,13,-87.0,Bezeq-NGN_E77EB9,cc:b2:55:e7:7e:ba,6,-87.0").split(",");
		WifiSample t2 = new WifiSample(s2);
		dataBase.add(t);
		dataBase.add(t2);
		int expected = 3;
		int actual = dataBase.getNumberOfMacs();
		assertEquals(actual, expected);

	}

}
