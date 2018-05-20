package test.java;


import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.java.csvFilter.SampleFilter;
import main.java.csvFilter.TimeFilter;
import main.java.dataObjects.WifiSample;

public class TimeFilterTest {
	TimeFilter filter;

	@Before
	public void setUp() throws Exception {
		filter = new TimeFilter("2017-12-03 08:49:20", "2017-12-03 08:49:50", "or");
	}

	@Test
	public void testToString() {
		String expected = "| Time(Sun Dec 03 08:49:20 IST 2017<date<Sun Dec 03 08:49:50 IST 2017)";
		String actual = filter.toString();
		assertEquals(actual, expected);
	}

	@Test
	public void testGetOperator() {
		String expected = "or";
		String actual = filter.getOperator();
		assertEquals(actual, expected);
	}

	@Test
	public void testGetStartTime() {
		String expected = "Sun Dec 03 08:49:20 IST 2017";
		String actual = filter.getStartTime().toString();
		assertEquals(actual, expected);
	}

	@Test
	public void testGetEndTime() {
		String expected = "Sun Dec 03 08:49:50 IST 2017";
		String actual = filter.getEndTime().toString();
		assertEquals(actual, expected);
	}

	@Test
	public void testFilter() {
		String[] s1 = ("2017-12-03 08:37:30,model=SM-G950F_device=dreamlte,"
				+ "32.30246378160256,35.207563401001366,694.129943277534" + ",1,Hoom_2.4,80:37:73:d7:ab:3a,1,-88.0")
						.split(",");
		WifiSample t1 = new WifiSample(s1);

		String[] s2 = ("2017-12-03 08:39:16,model=SM-G950F_device=dreamlte,"
				+ "32.106181954122206,35.21168763894033,689.1981253462078"
				+ ",2, ,00:1a:dd:f4:f5:c4,13,-87.0,Bezeq-NGN_E77EB9,cc:b2:55:e7:7e:ba,6,-87.0").split(",");
		WifiSample t2 = new WifiSample(s2);

		String[] s3 = ("2017-12-03 08:46:58,Redmi 4A," + "32.00389461,35.11035436,695.0"
				+ ",1,HelpDesk,60:e3:27:7b:e0:98,4,-87.0").split(",");
		WifiSample t3 = new WifiSample(s3);

		String[] s4 = ("2017-12-03 08:49:27,model=Lenovo PB2-690Y_device=PB2PRO," + "32.50497773,35.11118029,694.0,"
				+ "1,Ariel_University,1c:b9:c4:15:44:88,6,-72.0").split(",");
		WifiSample t4 = new WifiSample(s4);

		ArrayList<WifiSample> list = new ArrayList<WifiSample>();
		list.add(t1);
		list.add(t2);
		list.add(t3);
		list.add(t4);
		ArrayList<WifiSample> list2 = filter.filter(list);

		assertTrue(list2.size() == 1 && list2.get(0).toString().equals(t4.toString()));
	}

}
