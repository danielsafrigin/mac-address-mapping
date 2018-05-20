package main.java.csvFilter;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.stream.Collectors;

import main.java.dataObjects.WifiSample;

public class TimeFilter implements SampleFilter , Serializable{
	private String operator;
	private Date startTime;
	private Date endTime;

	public TimeFilter(String startTime, String endTime, String operator) throws ParseException {
		this.operator = operator;
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.startTime = format.parse(startTime);
		this.endTime = format.parse(endTime);
	}

	@Override
	public String toString() {
		String s = "Time(" + startTime + "<date<" + endTime + ")";
		if (operator.contains("not"))
			s = "!" + s;
		if (operator.contains("and"))
			s = "& " + s;
		else {
			s = "| " + s;
		}

		return s;
	}

	public String getOperator() {
		return operator;
	}

	public Date getStartTime() {
		return startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	@Override
	public ArrayList<WifiSample> filter(ArrayList<WifiSample> wifiSampleList) {
		return (ArrayList<WifiSample>) wifiSampleList.stream().filter(wifiSample -> {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date sampleDate = null;
			try {
				sampleDate = format.parse(wifiSample.getTimeStamp());
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return sampleDate.after(startTime) && sampleDate.before(endTime);
		}).collect(Collectors.toList());
	}
}
