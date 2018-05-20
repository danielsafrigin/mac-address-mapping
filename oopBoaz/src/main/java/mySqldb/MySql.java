package main.java.mySqldb;

/** 
 * This is a very simple example representing how to work with MySQL 
 * using java JDBC interface;
 * The example mainly present how to read a table representing a set of WiFi_Scans
 * Note: for simplicity only two properties are stored (in the DB) for each AP:
 * the MAC address (mac) and the signal strength (rssi), the other properties (ssid and channel)
 * are omitted as the algorithms do not use the additional data.
 * 
 */
import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import main.java.dataObjects.DataBase;
import main.java.dataObjects.WifiSample;
import main.java.dataObjects.WifiSpot;

import java.sql.Statement;

public class MySql {

	private static String _ip;
	private static String _url;
	private static String _user;
	private static String _password;
	private static Connection _con = null;
	private static DataBase mySqlData;


	public static String get_ip() {
		return _ip;
	}

	public static void set_ip(String _ip) {
		MySql._ip = _ip;
	}

	public static String get_url() {
		return _url;
	}

	public static void set_url(String _url) {
		MySql._url = _url;
	}

	public static String get_user() {
		return _user;
	}

	public static void set_user(String _user) {
		MySql._user = _user;
	}

	public static String get_password() {
		return _password;
	}

	public static void set_password(String _password) {
		MySql._password = _password;
	}

	public static Connection get_con() {
		return _con;
	}

	public static void set_con(Connection _con) {
		MySql._con = _con;
	}

	public DataBase getMySqlData() {
		return mySqlData;
	}

	public static void setMySqlData(DataBase mySqlData) {
		MySql.mySqlData = mySqlData;
	}

	public MySql(String ip, String url, String user, String password) {
		_ip = ip;
		_url = url;
		_user = user;
		_password = password;
		mySqlData = new DataBase();
		test_ex4_db();
	}

	public static void test_ex4_db() {

		Statement st = null;
		ResultSet rs = null;

		try {
			_con = DriverManager.getConnection(_url, _user, _password);
			st = _con.createStatement();
			rs = st.executeQuery(
					"SELECT UPDATE_TIME FROM information_schema.tables WHERE TABLE_SCHEMA = 'oop_course_ariel' AND TABLE_NAME = 'ex4_db'");
			if (rs.next()) {
				System.out.println("**** Update: " + rs.getString(1));
			}

			PreparedStatement pst = _con.prepareStatement("SELECT * FROM ex4_db");
		
			rs = pst.executeQuery();
			while (rs.next()) {
				int size = rs.getInt(7);
				int len = 7 + 2 * size;
				String line = "";
				for (int i = 1; i <= len - 1; i++) {
					line += rs.getString(i + 1) + ",";
				}
				String row[] = line.split(",");
				WifiSample tempSample = new WifiSample(row[0], row[1], Double.parseDouble(row[2]), Double.parseDouble(row[3]),
						Double.parseDouble(row[4]), row[5]);
				for(int i=6; i<row.length; i+=2){
					WifiSpot tempSpot = new WifiSpot("", row[i], -1,Integer.parseInt(row[i+1]));
					tempSample.addSpot(tempSpot);
				}
				
				mySqlData.add(tempSample);

			}
			
		} catch (SQLException ex) {
			Logger lgr = Logger.getLogger(MySql.class.getName());
			lgr.log(Level.SEVERE, ex.getMessage(), ex);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (st != null) {
					st.close();
				}
				if (_con != null) {
					_con.close();
				}
			} catch (SQLException ex) {

				Logger lgr = Logger.getLogger(MySql.class.getName());
				lgr.log(Level.WARNING, ex.getMessage(), ex);
			}
		}
	}

}