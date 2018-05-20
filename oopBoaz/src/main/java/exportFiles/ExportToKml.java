package main.java.exportFiles;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.micromata.opengis.kml.v_2_2_0.Document;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.Placemark;
import main.java.dataObjects.DataBase;
import main.java.dataObjects.WifiSample;
import main.java.dataObjects.WifiSpot;

/**
 * 
 * this class receives only "wifidata.csv" that was generated with
 * csvFileProcessing it takes WIFI spots from folder and generates a KML file
 * representing them with dots.
 * 
 * @author daniel
 */

public class ExportToKml {
	private String csvFilePath;



	/**
	 * Receives path of CSV file and filter then generates KML file
	 * @param csvFilePath path of CSV file
	 * @throws Exception file not found
	 */
	public ExportToKml(String csvFilePath) throws Exception {
		this.csvFilePath = csvFilePath;
		DataBase dataBase = new DataBase(this.csvFilePath);
		writeKmlFile(dataBase.getWifiSampleList());
	}
	
	public ExportToKml(DataBase dataBase) throws Exception {
		writeKmlFile(dataBase.getWifiSampleList());
	}


	/**
	 * Write samples into KML file
	 * 
	 * @param sampleList
	 */
	protected static void writeKmlFile(ArrayList<WifiSample> sampleList) {

		removeDuplicateSpots(sampleList);

		try {
			if (!sampleList.isEmpty()) {
				final Kml kml = new Kml();
				Document document = kml.createAndSetDocument().withName("toBoaz");
				Folder folder = document.createAndAddFolder();
				folder.withName("JoJo The Man").withOpen(true);

				for (int i = 0; i < sampleList.size(); i++) {
					double longitude = sampleList.get(i).getLongitude();
					double latitude = sampleList.get(i).getLatitude();
					String model = sampleList.get(i).getPhoneId();
					String time = fixTimeFormat(sampleList.get(i).getTimeStamp());
					for (int j = 0; j < (sampleList.get(i).getWifiList().size()); j++) {
						String ssid = sampleList.get(i).getWifiList().get(j).getSsid();
						String mac = sampleList.get(i).getWifiList().get(j).getMac();
						int frequncy = sampleList.get(i).getWifiList().get(j).getFrequncy();
						int signal = sampleList.get(i).getWifiList().get(j).getSignal();
						String description = "SSID: " + "<b>" + ssid + "</b>" + "<br/>" + "MAC: " + "<b>" + mac + "</b>"
								+ "<br/>" + "Time: " + "<b>" + time + "</b>" + "<br/>" + "Model: " + "<b>" + model
								+ "</b>" + "<br/>" + "Longitude: " + "<b>" + longitude + "</b>" + "<br/>" + "Latitude: "
								+ "<b>" + latitude + "</b>" + "<br/>" + "Frequncy: " + "<b>" + frequncy + "</b>"
								+ "<br/>" + "Signal: " + "<b>" + signal + "</b>";
						createPlacemarkWithChart(document, folder, longitude, latitude, ssid, 1, time, description);
					}
				}
				kml.marshal(new File("wifiDots.kml"));
				System.out.println("done!");
			}
		} catch (IOException ex) {
			System.out.print("Error processing file\n" + ex);
			System.exit(2);
		}
	}

	/**
	 * replace space (" ") with T and add Z in the end to fix time format
	 * 
	 * @param timeStamp
	 * @return
	 */
	protected static String fixTimeFormat(String timeStamp) {
		String arr[] = timeStamp.split(" ");
		return arr[0] + "T" + arr[1] + "Z";
	}

	/**
	 * Override function to include time stamp and description
	 * 
	 * @source: https://github.com/micromata/javaapiforkml/blob/master/src/test/java/de/micromata/jak/examples/Example1.java
	 * @param document
	 * @param folder
	 * @param longitude
	 * @param latitude
	 * @param ssid
	 * @param coveredLandmass
	 * @param timeStamp
	 * @param description
	 */
	protected static void createPlacemarkWithChart(Document document, Folder folder, double longitude, double latitude,
			String ssid, int coveredLandmass, String timeStamp, String description) {

		de.micromata.opengis.kml.v_2_2_0.Style style = document.createAndAddStyle();
		style.withId("style_" + ssid).createAndSetIconStyle().withScale(1.0);
		style.createAndSetLabelStyle().withColor("ff43b3ff").withScale(1.0);

		Placemark placemark = folder.createAndAddPlacemark();

		placemark.withName(ssid).withStyleUrl("#style_" + ssid)
				.withDescription("<![CDATA[BSSID: <b>" + ssid + description).createAndSetLookAt()
				.withLongitude(longitude).withLatitude(latitude).withAltitude(0).withRange(12000000);

		placemark.createAndSetPoint().addToCoordinates(longitude, latitude);
		placemark.createAndSetTimeStamp().setWhen(timeStamp);

	}

	/**
	 * Remove Duplicate WifiSpots
	 * 
	 * @param sampleList
	 */
	protected static void removeDuplicateSpots(ArrayList<WifiSample> sampleList) {

		Map<String, WifiSpot> macToSignalMap = new HashMap<>();

		List<ArrayList<WifiSpot>> wifiSpotLists = sampleList.stream().map(WifiSample::getWifiList)
				.collect(Collectors.toList());
		wifiSpotLists.forEach(wifiSpotList -> wifiSpotList.forEach(wifiSpot -> {
			if (macToSignalMap.containsKey(wifiSpot.getMac())) {
				if (wifiSpot.getSignal() > macToSignalMap.get(wifiSpot.getMac()).getSignal()) {
					macToSignalMap.put(wifiSpot.getMac(), wifiSpot);
				}
			} else {
				macToSignalMap.put(wifiSpot.getMac(), wifiSpot);
			}
		}));
		wifiSpotLists.forEach(wifiSpotList -> wifiSpotList
				.removeIf(wifiSpot -> !wifiSpot.equals(macToSignalMap.get(wifiSpot.getMac()))));

	}

}
