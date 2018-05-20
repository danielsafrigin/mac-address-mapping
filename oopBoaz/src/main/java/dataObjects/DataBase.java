package main.java.dataObjects;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import main.java.csvFilter.LocationFilterByRange;
import main.java.csvFilter.PhoneIdFilter;
import main.java.csvFilter.SampleFilter;
import main.java.csvFilter.TimeFilter;
import main.java.exportFiles.CsvFileProcessing;

/**
 * This database receives only files that was generated with fileProccesing.
 * creates an object containing a list of all WIFI samples from that file.
 * 
 * @author daniel
 */
public class DataBase {
	private ArrayList<WifiSample> wifiSampleList;
	private ArrayList<SampleFilter> filterList;

	// Settings:
	private final static String CSV_EXPECTED_HEADER = "Time,ID,Lat,Lon,Alt,#WiFi networks (up to 10),SSID1,MAC1,Frequncy1,Signal1,SSID2,MAC2,Frequncy2,Signal2,SSID3,MAC3,Frequncy3,Signal3,SSID4,MAC4,Frequncy4,Signal4,SSID5,MAC5,Frequncy5,Signal5,SSID6,MAC6,Frequncy6,Signal6,SSID7,MAC7,Frequncy7,Signal7,SSID8,MAC8,Frequncy8,Signal8,SSID9,MAC9,Frequncy9,Signal9,SSID10,MAC10,Frequncy10,Signal10";

	/**
	 * Compare by signal
	 */
	private static final Comparator<WifiSample> SAME_TIME_SAMPLE_LIST_COMPARATOR = new Comparator<WifiSample>() {
		@Override
		public int compare(WifiSample arg0, WifiSample arg1) {
			int ar0 = arg0.getWifiList().get(0).getSignal();
			int ar1 = arg1.getWifiList().get(0).getSignal();
			if (ar0 < ar1)
				return 1;
			if (ar0 > ar1)
				return -1;
			return 0;
		}
	};

	// Contractors
	/**
	 * Create empty Data base
	 */
	public DataBase() {
		this.wifiSampleList = new ArrayList<WifiSample>();
		this.filterList = new ArrayList<SampleFilter>();
	}

	/**
	 * Create Data base
	 * 
	 * @param csvFilePath
	 *            CSV file path
	 * @throws Exception
	 *             file not found
	 */
	public DataBase(String csvFilePath) throws Exception {
		this.wifiSampleList = ProcessCsvFile(csvFilePath);
		this.filterList = new ArrayList<SampleFilter>();
	}

	// DataBase FUNCTIONS
	/**
	 * add all Processed CSV files to From directory to Data base
	 * 
	 * @param path
	 *            of directory
	 * @throws Exception
	 *             file not found
	 */
	public void addFolder(String path) throws Exception {
		File directory = new File(path);
		File[] files = directory.listFiles();
		for (int i = 0; i < files.length; i++) {
			DataBase tempData = new DataBase(files[i].getPath());
			wifiSampleList.addAll(tempData.getWifiSampleList());
		}

	}

	/**
	 * Reload Data Base from wigleData and proccesedData folders (all filters
	 * will be gone)
	 * 
	 * @throws Exception
	 *             file not found
	 */
	public void reload() throws Exception {
		wifiSampleList.clear();
		filterList.clear();

		// clear temp data
		File temp = new File("dataBase\\wigleData\\temp");
		File tempfiles[] = temp.listFiles();
		for (int i = 0; i < tempfiles.length; i++) {
			tempfiles[i].delete();
		}
		// reload samples from wigle files
		File wigleFolder = new File("dataBase\\wigleData\\data");
		CsvFileProcessing file = new CsvFileProcessing(wigleFolder);
		addFolder("dataBase\\wigleData\\temp");
		// reload samples from processed csv files
		File csvFolder = new File("dataBase\\proccesedData");
		addFolder(csvFolder.getPath());

		System.out.println("database reload!");
	}

	/**
	 * Processed CSV file to arrayList of WIFI samples (can be filtered)
	 * 
	 * @return ArrayList of WifiSamples from CSV file
	 * @throws Exception
	 */
	protected ArrayList<WifiSample> ProcessCsvFile(String csvFilePath) throws Exception {
		FileReader fr = new FileReader(csvFilePath);
		BufferedReader br = new BufferedReader(fr);
		ArrayList<WifiSample> rowList = new ArrayList<WifiSample>();

		try {
			String fileHeader = br.readLine();
			String row = br.readLine();

			if (fileHeader.equals(CSV_EXPECTED_HEADER) && row != null) {
				while (row != null) {
					WifiSample r = new WifiSample(row.split(","));
					rowList.add(r);
					row = br.readLine();
				}
			}
		} catch (IOException ex) {
			System.out.print("Error reading file\n" + ex);
			System.exit(2);
		} finally {
			br.close();
			fr.close();
		}

		return rowList;
	}

	/**
	 * add filter to Data base
	 * 
	 * @param filter
	 *            filter
	 */
	public void addFilter(SampleFilter filter) {
		this.filterList.add(filter);
	}

	/**
	 * save filter list as SER file
	 * 
	 * @throws IOException
	 *             can't write/ file not found
	 */
	public void saveFilter() throws IOException {
		File file = new File("filter.ser");
		file.delete();
		FileOutputStream fout = new FileOutputStream("filter.ser", true);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(filterList);
		fout.close();
		oos.close();

	}

	/**
	 * load filter from SER file
	 * 
	 * @param serializedFilterPath
	 *            path of serialized filter file
	 * @throws IOException
	 *             can't write file
	 * @throws ClassNotFoundException
	 *             class not found
	 */
	public void loadFilter(String serializedFilterPath) throws IOException, ClassNotFoundException {
		File filterFile = new File(serializedFilterPath);
		if (filterFile.isFile() && filterFile.canRead()) {
			FileInputStream fis = new FileInputStream(filterFile);
			ObjectInputStream ois = new ObjectInputStream(fis);
			filterList.clear();
			filterList.addAll((ArrayList<SampleFilter>) ois.readObject());
			fis.close();
			ois.close();
		} else {
			System.out.println("problem with input serializedFilterPath");
		}

	}

	/**
	 * 
	 * @return filtered list of samples
	 */
	public ArrayList<WifiSample> generateFilteredData() {
		DataBase filteredDataBase = new DataBase();
		for (int i = 0; i < filterList.size(); i++) {
			ArrayList<WifiSample> tempData = filterList.get(i).filter(wifiSampleList);
			if (filterList.get(i).getOperator().contains("not")) {
				ArrayList<WifiSample> tempData2 = new ArrayList<WifiSample>();
				for (int j = 0; j < wifiSampleList.size(); j++) {
					if (!tempData.contains(wifiSampleList.get(j))) {
						tempData2.add(wifiSampleList.get(j));
					}
				}
				tempData = tempData2;
			}
			if (filterList.get(i).getOperator().contains("or")) {
				filteredDataBase.addAll(tempData);
			} else if (filterList.get(i).getOperator().contains("and")) {
				for (int j = 0; j < filteredDataBase.getWifiSampleList().size(); j++) {
					if (!tempData.contains(filteredDataBase.getWifiSampleList().get(j))) {
						filteredDataBase.getWifiSampleList().remove(j);
						j--;
					}
				}

			}
		}
		System.out.println("success! generated new filtered data");
		return filteredDataBase.getWifiSampleList();
	}

	/**
	 * add a WIFI Sample
	 * 
	 * @param wifiSample
	 *            wifi sample
	 */
	public void add(WifiSample wifiSample) {
		this.wifiSampleList.add(wifiSample);
	}

	/**
	 * add all WIfi Samples from List
	 * 
	 * @param wifiSampleList
	 *            list of wifi samples
	 */
	public void addAll(ArrayList<WifiSample> wifiSampleList) {
		this.wifiSampleList.addAll(wifiSampleList);
	}

	/**
	 * add all WIFI Samples from Data base
	 * 
	 * @param dataBase
	 *            Data base
	 */
	public void addAll(DataBase dataBase) {
		this.wifiSampleList.addAll(dataBase.getWifiSampleList());
	}

	/**
	 * remove all WIFI samples from Data base
	 */
	public void clear() {
		this.wifiSampleList.clear();
	}

	/**
	 * Write CSV file from a given list of WIFI samples and the output file name
	 * 
	 * @param outputFileName
	 *            name of output file
	 * @throws IOException
	 *             can't write to file
	 */
	public void WriteSamplesToFile(String outputFileName) throws IOException {
		PrintWriter pw = new PrintWriter(new File(outputFileName));
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < wifiSampleList.size(); i++) {
			String csvLine1 = String.format("%s,%s,%s,%s,%s,%s,", wifiSampleList.get(i).getTimeStamp(),
					wifiSampleList.get(i).getPhoneId(), wifiSampleList.get(i).getLatitude() + "",
					wifiSampleList.get(i).getLongitude() + "", wifiSampleList.get(i).getAltitude() + "",
					wifiSampleList.get(i).getWifiList().size() + "");
			sb.append(csvLine1);
			for (int j = 0; j < wifiSampleList.get(i).getWifiList().size(); j++) {
				String csvLine2 = String.format("%s,%s,%s,%s,", wifiSampleList.get(i).getWifiList().get(j).getMac(),
						wifiSampleList.get(i).getWifiList().get(j).getSsid(),
						wifiSampleList.get(i).getWifiList().get(j).getFrequncy() + "",
						wifiSampleList.get(i).getWifiList().get(j).getSignal() + "");
				sb.append(csvLine2);
			}

			if (i != wifiSampleList.size() - 1)
				sb.append("\n");
		}
		pw.write(sb.toString());
		pw.close();
		System.out.println("done!");
	}

	// SETTERS AND GETTERS
	/**
	 * 
	 * @return list of all filters in Data base
	 */
	public ArrayList<SampleFilter> getFilterList() {
		return filterList;
	}

	public String filterListToString() {
		String filter = "";
		for (int i = 0; i < filterList.size(); i++) {
			filter += filterList.get(i).toString();
		}
		return filter;
	}

	/**
	 * 
	 * @return list of all WIFI samples in Data base
	 */
	public ArrayList<WifiSample> getWifiSampleList() {
		return wifiSampleList;
	}

	/**
	 * 
	 * @return number of WIFI samples in Data base
	 */
	public int size() {
		return wifiSampleList.size();
	}

	/**
	 * 
	 * @return number of MAC addresses in Data base
	 */
	public int getNumberOfMacs() {
		int size = 0;
		for (int i = 0; i < wifiSampleList.size(); i++) {
			size += wifiSampleList.get(i).getWifiList().size();
		}
		return size;
	}

	// FIND LOCATION
	/**
	 * find my location based on algorithem1
	 * 
	 * @param mac
	 *            address
	 * @return String containing lat lon and alt of location
	 * @throws ParseException
	 *             parse exception
	 */
	public String findMyLocation(String mac) throws ParseException {
		ArrayList<WifiSample> wifiSampleListBySpots = wifiSpotsToWifiSamples(this.wifiSampleList);
		ArrayList<WifiSample> tempWifiSampleList = new ArrayList<WifiSample>();
		for (int j = 0; j < wifiSampleListBySpots.size(); j++) {
			if (mac.equals(wifiSampleListBySpots.get(j).getWifiList().get(0).getMac())) {
				tempWifiSampleList.add(wifiSampleListBySpots.get(j));
			}
		}
		keepStrongestBySignal(tempWifiSampleList);
		WifiSample finalSample = (algorithem1(tempWifiSampleList));
		return "lat:" + finalSample.getLatitude() + " lon:" + finalSample.getLongitude() + " alt:"
				+ finalSample.getAltitude();
	}

	/**
	 * find my location based on algorithem2
	 * 
	 * @param mac1
	 *            mac
	 * @param mac2
	 *            mac
	 * @param mac3
	 *            mac
	 * @param signal1
	 *            signal
	 * @param signal2
	 *            signal
	 * @param signal3
	 *            signal
	 * @return String containing lat lon alt of location
	 * @throws ParseException
	 *             parse exception
	 */
	public String findMyLocation2(String mac1, String mac2, String mac3, String signal1, String signal2, String signal3)
			throws ParseException {
		WifiSample scan = new WifiSample(mac1, mac2, mac3, signal1, signal2, signal3);
		ArrayList<WifiSample> tempWifiSampleList = piSmiliarityList(scan, this.wifiSampleList, 3);
		WifiSample finalSample = algorithem2(scan, tempWifiSampleList);
		return "lat:" + finalSample.getLatitude() + " lon:" + finalSample.getLongitude() + " alt:"
				+ finalSample.getAltitude();

	}

	/**
	 * find my location based on algorithem2
	 * 
	 * @param WifisampleString
	 *            wifi sample represented as string
	 * @return String containing lat lon alt of location
	 * @throws ParseException
	 *             parse exception
	 */
	public String findMyLocation2(String WifisampleString) throws ParseException {
		WifiSample scan = new WifiSample(WifisampleString.split(","));
		ArrayList<WifiSample> tempWifiSampleList = piSmiliarityList(scan, this.wifiSampleList, 3);
		WifiSample finalSample = algorithem2(scan, tempWifiSampleList);
		return "lat:" + finalSample.getLatitude() + " lon:" + finalSample.getLongitude() + " alt:"
				+ finalSample.getAltitude();

	}

	// ALOGRITHEM1 FUNCTIONS
	/**
	 * Create and Write a list of WIFI samples based on algorithem1
	 * 
	 * @param outputFileName
	 *            output name of file
	 * @throws IOException
	 *             can't write file
	 * @throws ParseException
	 *             parse exception
	 */
	public void exportAlgorithem1(String outputFileName) throws IOException, ParseException {
		ArrayList<WifiSample> wifiSampleListBySpots = wifiSpotsToWifiSamples(this.wifiSampleList);
		ArrayList<WifiSample> finalWifiSampleList = new ArrayList<WifiSample>();
		for (int i = 0; i < wifiSampleListBySpots.size(); i++) {
			ArrayList<WifiSample> tempWifiSampleList = new ArrayList<WifiSample>();
			tempWifiSampleList.add(wifiSampleListBySpots.get(i));
			wifiSampleListBySpots.remove(i);
			for (int j = 0; j < wifiSampleListBySpots.size(); j++) {
				if (tempWifiSampleList.get(0).getWifiList().get(0).getMac()
						.equals(wifiSampleListBySpots.get(j).getWifiList().get(0).getMac())) {
					tempWifiSampleList.add(wifiSampleListBySpots.get(j));
				}
			}
			i--;
			keepStrongestBySignal(tempWifiSampleList);
			finalWifiSampleList.add(algorithem1(tempWifiSampleList));

			tempWifiSampleList.clear();
		}
		removeDuplicateSamples(finalWifiSampleList);
		DataBase tempData = new DataBase();
		tempData.addAll(finalWifiSampleList);
		tempData.WriteSamplesToFile(outputFileName);
	}

	/**
	 * Remove any Duplicate WIFI samples in a given list
	 * 
	 * @param WifiSampleList
	 */
	private void removeDuplicateSamples(ArrayList<WifiSample> WifiSampleList) {
		Map<String, WifiSample> macToSignalMap = new HashMap<>();

		for (int i = 0; i < WifiSampleList.size(); i++) {
			if (macToSignalMap.containsKey(WifiSampleList.get(i).getWifiList().get(0).getMac())) {
				WifiSampleList.remove(i);
				i--;
			} else {
				macToSignalMap.put(WifiSampleList.get(i).getWifiList().get(0).getMac(), WifiSampleList.get(i));
			}
		}

	}

	/**
	 * Generate ArrayList of WifiSamples From WifiSpots of a given WifiSample
	 * ArrayList
	 * 
	 * @param wifiSampleList
	 * @return list of generated samples of all points from a list of WIFI
	 *         samples
	 * @throws ParseException
	 */
	private static ArrayList<WifiSample> wifiSpotsToWifiSamples(ArrayList<WifiSample> wifiSampleList)
			throws ParseException {
		ArrayList<WifiSample> wifiSampleListBySpots = new ArrayList<WifiSample>();
		for (int i = 0; i < wifiSampleList.size(); i++) {
			wifiSampleListBySpots.addAll(splitWifiSample(wifiSampleList.get(i)));
		}
		return wifiSampleListBySpots;
	}

	/**
	 * Split all points of a given WIFI sample into WIFI samples
	 * 
	 * @param wifiSample
	 * @return list of generated samples of all points from a WIFI sample
	 * @throws ParseException
	 */
	private static ArrayList<WifiSample> splitWifiSample(WifiSample wifiSample) throws ParseException {
		ArrayList<WifiSample> wifiSampleListBySpots = new ArrayList<WifiSample>();
		for (int i = 0; i < wifiSample.getWifiList().size(); i++) {
			WifiSample sample = new WifiSample(wifiSample, i);
			wifiSampleListBySpots.add(sample);
		}
		return wifiSampleListBySpots;
	}

	/**
	 * Keep only the 4 strongest WIFI samples of a given WIFI sample list by
	 * signal
	 * 
	 * @param wifiSampleList
	 */
	private static void keepStrongestBySignal(ArrayList<WifiSample> wifiSampleList) {
		Collections.sort(wifiSampleList, SAME_TIME_SAMPLE_LIST_COMPARATOR);
		while (wifiSampleList.size() > 4) {
			wifiSampleList.remove(4);
		}
	}

	/**
	 * Creating new point by calculating algorithem1 on a given similar WIFI
	 * sample list
	 * 
	 * @param wifiSampleList
	 * @return WIFI sample by algorithem1
	 * @throws ParseException
	 */
	private static WifiSample algorithem1(ArrayList<WifiSample> wifiSampleList) throws ParseException {
		double sumWLat = 0;
		double sumWLon = 0;
		double sumWAlt = 0;
		double sumWeight = 0;
		for (int i = 0; i < wifiSampleList.size(); i++) {
			double signal = wifiSampleList.get(i).getWifiList().get(0).getSignal();
			double lat = wifiSampleList.get(i).getLatitude();
			double lon = wifiSampleList.get(i).getLongitude();
			double alt = wifiSampleList.get(i).getAltitude();
			double weight = 1.0 / (signal * signal);
			sumWLat += (lat * weight);
			sumWLon += (lon * weight);
			sumWAlt += (alt * weight);
			sumWeight += (weight);
		}

		double finalLat = sumWLat / sumWeight;
		double finalLon = sumWLon / sumWeight;
		double finalAlt = sumWAlt / sumWeight;

		WifiSpot wifiSpot = new WifiSpot(wifiSampleList.get(0).getWifiList().get(0).getSsid(),
				wifiSampleList.get(0).getWifiList().get(0).getMac(),
				wifiSampleList.get(0).getWifiList().get(0).getFrequncy(),
				wifiSampleList.get(0).getWifiList().get(0).getSignal());

		WifiSample wifiSample = new WifiSample(wifiSampleList.get(0).getTimeStamp(), wifiSampleList.get(0).getPhoneId(),
				finalLat, finalLon, finalAlt, wifiSampleList.get(0).getWifiList().get(0).getSsid());
		wifiSample.addSpot(wifiSpot);

		return wifiSample;
	}

	// ALOGRITHEM2 FUNCTIONS
	/**
	 * Create and Write a list of WIFI samples based on algorithem2
	 * 
	 * @param dataBase
	 *            data base
	 * @param k
	 *            number of macs to compare
	 * @param outputFileName
	 *            name of output file
	 * @throws IOException
	 *             can't write file
	 * @throws ParseException
	 *             parse exception
	 */
	public void exportAlgorithem2(DataBase dataBase, int k, String outputFileName) throws IOException, ParseException {
		ArrayList<WifiSample> locationList = new ArrayList<WifiSample>();
		for (int i = 0; i < wifiSampleList.size(); i++) {
			ArrayList<WifiSample> tempWifiSampleList = piSmiliarityList(this.wifiSampleList.get(i),
					dataBase.getWifiSampleList(), k);
			locationList.add(algorithem2(this.wifiSampleList.get(i), tempWifiSampleList));
		}
		DataBase tempData = new DataBase();
		tempData.addAll(locationList);
		tempData.WriteSamplesToFile(outputFileName);
	}

	/**
	 * Create a list of the k most similar WIFI samples by pi calculation
	 * 
	 * @param wifiSample
	 * @param dataBase
	 * @param k
	 * @return List of the k most similar WIFI samples
	 */
	private ArrayList<WifiSample> piSmiliarityList(WifiSample wifiSample, ArrayList<WifiSample> data, int k) {

		// ArrayList<WifiSample> data = dataBase.getWifiSampleList();
		ArrayList<WifiSample> templist = new ArrayList<WifiSample>();
		for (int i = 0; i < k; i++) {
			double maxPi = 0;
			int maxPiIndex = 0;
			for (int j = i; j < data.size(); j++) {
				if (pi(wifiSample, data.get(j)) > maxPi) {
					maxPi = pi(wifiSample, data.get(j));
					maxPiIndex = j;
				}
			}
			templist.add(data.get(maxPiIndex));
			Collections.swap(data, i, maxPiIndex);
		}
		return templist;
	}

	/**
	 * Calculate the pi value by comparing wifiSample2 to wifiSample
	 * 
	 * @param wifiSample
	 * @param wifiSample2
	 * @return pi value of the 2 WIFI samples
	 */
	private static double pi(WifiSample wifiSample, WifiSample wifiSample2) {
		Map<String, WifiSpot> macToSignalMap = new HashMap<>();
		for (int i = 0; i < wifiSample2.getWifiList().size(); i++) {
			macToSignalMap.put(wifiSample2.getWifiList().get(i).getMac(), wifiSample2.getWifiList().get(i));
		}
		boolean containsSharedPoint = false;
		double weight = 1;

		for (int i = 0; i < wifiSample.getWifiList().size(); i++) {
			int scanSignal = wifiSample.getWifiList().get(i).getSignal();

			if (macToSignalMap.containsKey(wifiSample.getWifiList().get(i).getMac())) {
				containsSharedPoint = true;
				int dataSignal = macToSignalMap.get(wifiSample.getWifiList().get(i).getMac()).getSignal();
				int difSignal = Math.abs(dataSignal - scanSignal) + 3;
				weight *= 10000.0 / (Math.pow(difSignal, 0.4) * scanSignal * scanSignal);
			} else {
				int difSignal = 100;
				weight *= 10000.0 / (Math.pow(difSignal, 0.4) * scanSignal * scanSignal);
			}
		}
		if (containsSharedPoint)
			return weight;
		return 0;
	}

	/**
	 * Creating new point by calculating algorithem2 on a given similar WIFI
	 * sample list
	 * 
	 * @param wifiSample
	 * @param wifiSampleListByMac1
	 * @param wifiSampleListByMac3
	 * @param wifiSampleListByMac2
	 * @param signalList
	 * @return WIFI sample by algorithem2
	 * @throws ParseException
	 */
	private static WifiSample algorithem2(WifiSample wifiSample, ArrayList<WifiSample> WifiSampleList)
			throws ParseException {
		double sumLat = 0;
		double sumLon = 0;
		double sumAlt = 0;
		double sumPi = 0;

		for (int i = 0; i < WifiSampleList.size(); i++) {
			double pi = pi(wifiSample, WifiSampleList.get(i));

			sumPi += pi;
			if (pi > 0) {
				sumLat += WifiSampleList.get(i).getLatitude() * pi;
				sumLon += WifiSampleList.get(i).getLongitude() * pi;
				sumAlt += WifiSampleList.get(i).getAltitude() * pi;
			}

		}

		double finalLat = sumLat / sumPi;
		double finalLon = sumLon / sumPi;
		double finalAlt = sumAlt / sumPi;

		WifiSample wifiSample2 = new WifiSample(wifiSample);
		wifiSample2.setAltitude(finalAlt);
		wifiSample2.setLatitude(finalLat);
		wifiSample2.setLongitude(finalLon);

		return wifiSample2;
	}

}
