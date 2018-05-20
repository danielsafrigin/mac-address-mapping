package main.java.csvFilter;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.stream.Collectors;

import main.java.dataObjects.WifiSample;

public class PhoneIdFilter implements SampleFilter, Serializable {
	private String operator;
	private String phoneId;

	public PhoneIdFilter(String PhoneId, String operator) {
		this.operator = operator;
		this.phoneId = PhoneId;
	}

	public String getOperator() {
		return operator;
	}

	public String getPhoneId() {
		return phoneId;
	}

	@Override
	public String toString() {
		String s = "Device(" + phoneId + ")";
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
		return (ArrayList<WifiSample>) wifiSampleList.stream()
				.filter(wifiSample -> wifiSample.getPhoneId().equals(phoneId)).collect(Collectors.toList());
	}

}
