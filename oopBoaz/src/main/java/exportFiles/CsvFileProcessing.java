package main.java.exportFiles;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * this class receives a folder, merges all relevant CSV files and generates a
 * new one with all the relevant information
 * 
 * @author daniel
 */

public class CsvFileProcessing {
	private File folderOfCsvFiles;
	private String outputName;

	// Settings:
	private static final int CSV_PHONE_INFO_ROW_NUMBER_OF_ELEMENTS = 8;
	private static final int CSV_PHONE_MODEL_INDEX = 2;
	private static final int CSV_NUMBER_OF_ELEMENTS_PER_LINE = 11;
	private static final int CSV_TIME_STAMP_INDEX = 3;
	private static final int CSV_RSSI_INDEX = 5;
	private static final int CSV_LAT_INDEX = 6;
	private static final int CSV_LON_INDEX = 7;
	private static final int CSV_ALT_INDEX = 8;
	private static final int CSV_MAC_INDEX = 0;
	private static final int CSV_SSID_INDEX = 1;
	private static final int CSV_CHANNEL_INDEX = 4;
	private static final String[] NEW_CSV_TAGS = new String[] { "Time", "ID", "Lat", "Lon", "Alt",
			"#WiFi networks (up to 10)", "SSID1", "MAC1", "Frequncy1", "Signal1", "SSID2", "MAC2", "Frequncy2",
			"Signal2", "SSID3", "MAC3", "Frequncy3", "Signal3", "SSID4", "MAC4", "Frequncy4", "Signal4", "SSID5",
			"MAC5", "Frequncy5", "Signal5", "SSID6", "MAC6", "Frequncy6", "Signal6", "SSID7", "MAC7", "Frequncy7",
			"Signal7", "SSID8", "MAC8", "Frequncy8", "Signal8", "SSID9", "MAC9", "Frequncy9", "Signal9", "SSID10",
			"MAC10", "Frequncy10", "Signal10" };

	private static final Comparator<String[]> SAME_TIME_SAMPLE_LIST_COMPARATOR = new Comparator<String[]>() {
		@Override
		public int compare(String[] arg0, String[] arg1) {
			int ar0 = Integer.parseInt(arg0[CSV_RSSI_INDEX]);
			int ar1 = Integer.parseInt(arg1[CSV_RSSI_INDEX]);
			if (ar0 < ar1)
				return 1;
			if (ar0 > ar1)
				return -1;
			return 0;
		}
	};

	/**
	 * Constructor receives directory containing CSV files
	 * @param folderOfCsvFiles path of folder
	 * @throws Exception file not found
	 */
	public CsvFileProcessing(File folderOfCsvFiles) throws Exception {
		this.folderOfCsvFiles = folderOfCsvFiles;

		processFolder();
	}

	/**
	 * Process all CSV files in directory
	 * 
	 * @throws Exception
	 */
	protected void processFolder() throws Exception {
		if (this.folderOfCsvFiles.isDirectory() && this.folderOfCsvFiles.canRead()) {
			File folderFileList[] = this.folderOfCsvFiles.listFiles();
			ArrayList<String> mergedFile = mergeFile(folderFileList);
			writeFileToCsv(readRawCsvFile(mergedFile));
		} else {
			System.out.println("bad path");
		}
	}

	/**
	 * Merge all files of CSV format in directory
	 * 
	 * @param folderFileList
	 * @return merged file
	 * @throws Exception
	 */
	protected static ArrayList<String> mergeFile(File[] folderFileList) throws Exception {
		ArrayList<String> rowList = new ArrayList<String>();
		for (int i = 0; i < folderFileList.length; i++) {
			String file = folderFileList[i].getPath();
			if (checkFormat(file)) {
				ArrayList<String> tempRowList = new ArrayList<String>();
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				try {
					String row = br.readLine();
					while (row != null) {
						tempRowList.add(row);
						row = br.readLine();
					}
					if (tempRowList.size() > 3) {
						rowList.addAll(tempRowList);
					}
				} catch (IOException ex) {
					System.out.print("Error merging file\n" + ex);
					System.exit(2);
				} finally {
					br.close();
					fr.close();
				}
			}
		}
		return rowList;
	}

	/**
	 * Read merged file and split each line into String[]
	 * 
	 * @param mergedFile
	 * @return ArrayList<String[]> of split lines
	 */
	protected static ArrayList<String[]> readRawCsvFile(ArrayList<String> mergedFile) {
		ArrayList<String[]> rowList = new ArrayList<String[]>();
		while (!mergedFile.isEmpty()) {
			String[] r = mergedFile.get(0).split(",");
			if (r.length == CSV_NUMBER_OF_ELEMENTS_PER_LINE || r.length == CSV_PHONE_INFO_ROW_NUMBER_OF_ELEMENTS)
				rowList.add(r);
			mergedFile.remove(0);
		}
		return rowList;
	}

	/**
	 * Read CSV file and write a new one by tags requested
	 * 
	 * @param rowList
	 * @throws Exception
	 */
	protected void writeFileToCsv(ArrayList<String[]> rowList) throws Exception {
		// source:
		// https://stackoverflow.com/questions/30073980/java-writing-strings-to-a-csv-file
		File folder = new File("dataBase\\wigleData\\temp");
		String fileName = "";
		
		fileName = "dataBase\\wigleData\\temp\\wifidata"+folder.listFiles().length+".csv";
		outputName = fileName;
		PrintWriter pw = new PrintWriter(new File(fileName));
		StringBuilder sb = new StringBuilder();

		initializeTags(sb);

		// combine all WIFI listings with the same time stamp then writing them
		// into a CSV file one by one
		ArrayList<String[]> sameTimeList = new ArrayList<String[]>();
		String phoneId = "None";
		while (rowList.size() > 2) {
			if (rowList.get(0).length == CSV_PHONE_INFO_ROW_NUMBER_OF_ELEMENTS) {
				phoneId = rowList.get(0)[CSV_PHONE_MODEL_INDEX].split("=")[1];
				rowList.remove(0);
				rowList.remove(0);
			}
			sameTimeList.add(rowList.get(0));
			rowList.remove(0);
			for (int i = 0; i < rowList.size(); i++) {
				if (rowList.get(i)[CSV_TIME_STAMP_INDEX].equals(sameTimeList.get(0)[CSV_TIME_STAMP_INDEX])) {
					sameTimeList.add(rowList.get(i));
					rowList.remove(i);
					i--;
				}
			}

			// source:
			// https://stackoverflow.com/questions/16751540/sorting-an-object-arraylist-by-an-attribute-value-in-java
			// sort by network signal
			Collections.sort(sameTimeList, SAME_TIME_SAMPLE_LIST_COMPARATOR);

			// remove all networks after the top 10 nearest ones
			while (sameTimeList.size() > 10) {
				sameTimeList.remove(10);
			}

			// writing each of the samples to the new CSV file
			String timeStamp = sameTimeList.get(0)[CSV_TIME_STAMP_INDEX];
			String lat = sameTimeList.get(0)[CSV_LAT_INDEX];
			String lon = sameTimeList.get(0)[CSV_LON_INDEX];
			String alt = sameTimeList.get(0)[CSV_ALT_INDEX];
			String networkNames = returnNetworkNames(sameTimeList);

			String NewLinePart1 = String.format("\n%s,%s,%s,%s,%s,%s,", timeStamp, phoneId, lat, lon, alt,
					networkNames);
			sb.append(NewLinePart1);

			for (int i = 0; i < sameTimeList.size(); i++) {
				String ssid = sameTimeList.get(i)[CSV_SSID_INDEX];
				String mac = sameTimeList.get(i)[CSV_MAC_INDEX];
				String channel = sameTimeList.get(i)[CSV_CHANNEL_INDEX];
				String rssi = sameTimeList.get(i)[CSV_RSSI_INDEX];
				String NewLinePart2 = String.format("%s,%s,%s,%s", ssid, mac, channel, rssi);
				sb.append(NewLinePart2);
				if (i + 1 != sameTimeList.size())
					sb.append(',');
			}
			sameTimeList.clear();
		}
		pw.write(sb.toString());
		pw.close();
		System.out.println("done!");
	}

	/**
	 * Add tags to the new CSV file
	 * 
	 * @param sb
	 */
	protected static void initializeTags(StringBuilder sb) {
		for (int i = 0; i < 46; i++) {
			sb.append(NEW_CSV_TAGS[i]);
			if (i != 45)
				sb.append(',');
		}
	}

	/**
	 * Check if the format of the path's file is a CSV
	 * 
	 * @param path
	 * @return true if CSV file, else false
	 */
	protected static boolean checkFormat(String path) {
		String[] arr = path.split("/");
		String fileName = arr[arr.length - 1];
		if (fileName.contains(".csv")) {
			return true;
		}
		return false;
	}

	/**
	 * Combine all WIFI names of a sample
	 * 
	 * @param arraylist
	 * @return string of all WIFI networks names
	 */
	protected static String returnNetworkNames(ArrayList<String[]> arraylist) {
		String wifiNameList = "";
		for (int i = 0; i < arraylist.size(); i++) {
			if (i + 1 < arraylist.size())
				wifiNameList += arraylist.get(i)[CSV_SSID_INDEX] + " ";
			else
				wifiNameList += arraylist.get(i)[CSV_SSID_INDEX];

		}
		return wifiNameList;
	}
	
	public String getOutputName() {
		return outputName;
	}

}
