package test.java;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import main.java.csvFilter.LocationFilterByRadius;
import main.java.csvFilter.LocationFilterByRange;
import main.java.csvFilter.PhoneIdFilter;
import main.java.csvFilter.SampleFilter;
import main.java.csvFilter.TimeFilter;
import main.java.dataObjects.DataBase;

public class Examples {

	public static void main(String[] args) throws Exception {


		// example of fileProccesing

		// File folder = new File("C:\\Users\\daniel\\Downloads\\oop-0");
		// CsvFileProccesing file = new CsvFileProccesing(folder);

		// examples of ProccesedToKML and Filter, pick one of the examples below
		// uri of the new CSV file
		// ProccesedToKML kml = new
		// ProccesedToKML("C:\\Users\\daniel\\git\\year2-sm1-oop\\oopBoaz\\wifidata.csv",
		// null);

		// CsvFilter filter = new CsvFilter("time", "2017-10-29 00:46:08");
		// ProccesedToKML kml = new
		// ProccesedToKML("C:\\Users\\daniel\\git\\year2-sm1-oop\\oopBoaz\\wifidata.csv",
		// filter);

		// CsvFilter filter = new CsvFilter("phoneId", "MI 4LTE");
		// ProccesedToKML kml = new
		// ProccesedToKML("C:\\Users\\daniel\\git\\year2-sm1-oop\\oopBoaz\\wifidata.csv",
		// filter);

		// CsvFilter filter = new CsvFilter("location", 32.1608, 34.80613,
		// 0.01);
		// ProccesedToKML kml = new
		// ProccesedToKML("C:\\Users\\daniel\\git\\year2-sm1-oop\\oopBoaz\\wifidata.csv",
		// filter);

		// Ex2
		
		// test for algorithem 1
		// DataBase algo1 = new ProccesedToWifiSampleList(
		// "C:\\Users\\daniel\\git\\year2-sm1-oop\\oopBoaz\\wifidata.csv",
		// null);
		// algo1.exportAlgorithem1("algo1output.csv");

		// test for algorithem 2
		// DataBase dataBase = new ProccesedToWifiSampleList(
		// "C:\\Users\\daniel\\git\\year2-sm1-oop\\oopBoaz\\database.csv",
		// null);
		//
		// DataBase srikot = new ProccesedToWifiSampleList(
		// "C:\\Users\\daniel\\git\\year2-sm1-oop\\oopBoaz\\scan.csv", null);
		// srikot.exportAlgorithem2(dataBase, 4, "algo2output.csv");
		
		
		
		//Ex3
		// example for each of the inputs
		
		/*
		add wigle scan = docs\\project report ex 0 and 1
		add processed csv = docs\\project report ex2\\algo1\\wifidata.csv
		
		time filter = 2017-12-03 08:49:20 -> 2017-12-03 08:49:50
		filter by location = {
		min lat = 32.1  | max lat = 32.11
		min lon = 35.21  | max lon = 35.22
		min alt = 688.0  | max alt = 690
		}
		filter by phone = model=SM-G950F_device=dreamlte
		add filter = filter.ser
		
		
		
		algo1 = 1c:b9:c4:16:05:78
		algo2 = 34:8f:27:20:89:b8 34:8f:27:20:89:bc 34:8f:27:a0:89:b7 -50 -53 -54
		algo2 = 12/05/17 11:33 AM,model=SM-G950F_device=dreamlte,1,1,1,1,Ariel_University,24:79:2a:2b:07:b8,11,-57
		
		*/
		
	}

}
