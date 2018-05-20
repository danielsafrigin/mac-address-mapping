package main.java.gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;

import main.java.csvFilter.LocationFilterByRadius;
import main.java.csvFilter.LocationFilterByRange;
import main.java.csvFilter.PhoneIdFilter;
import main.java.csvFilter.SampleFilter;
import main.java.csvFilter.TimeFilter;
import main.java.dataObjects.DataBase;
import main.java.exportFiles.CsvFileProcessing;
import main.java.exportFiles.ExportToKml;
import main.java.mySqldb.MySql;

import javax.swing.JRadioButton;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.HeadlessException;

public class Gui {

	private JFrame frame;
	public static JLabel lblStats;
	private JTextField txtPathOfRawCsv;
	private JTextField txtPathOfProcessedCsv;
	private JButton button3;
	private JButton button4;
	private JButton btnFilterByName;
	private JTextField txtStartTime;
	private JTextField txtEndTime;
	public static String filterStats = "";
	private JButton btnFilterByLocation;
	private JTextField txtMinLat;
	private JTextField txtPhoneId;
	private JTextField txtMaxLat;
	private JTextField txtMinLon;
	private JTextField txtMaxLon;
	private JTextField txtMaxAlt;
	private JTextField txtMinAlt;
	private JRadioButton rdbtnAnd;
	private JRadioButton rdbtnOr;
	private JRadioButton rdbtnNot;
	private JButton btnClearFilter;
	private JButton btnAddFilter;
	private JTextField txtPathOfFilter;
	private JButton btnFindMyLocation;
	private JTextField txtMac;
	private JButton btnFindMyLocation_1;
	private JTextField txtalgo2op1;
	private JButton btnFindMyLocation_2;
	private JTextField txtalgo2op2;
	private JLabel lblNewLabel;
	public static int x = 0;
	private JButton btnSaveFilter;
	private JTextField txtIp;
	private JTextField txtUrl;
	private JTextField txtUser;
	private JTextField txtPassword;

	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Launch the application.
	 */
	public Gui() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1280, 720);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		DataBase dataBase = new DataBase();
		DataBase filteredDataBase = new DataBase();

		lblStats = new JLabel("stats: none");
		lblStats.setFont(new Font("Tahoma", Font.PLAIN, 16));
		lblStats.setBounds(12, 511, 1238, 54);
		frame.getContentPane().add(lblStats);

		CheckForUpdates updatesThread = new CheckForUpdates(dataBase, filteredDataBase);
		updatesThread.start();
		try {
			filteredDataBase.clear();
			dataBase.reload();
			Gui.updateStats(dataBase, Gui.lblStats);
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

		JButton btnAddWigleScan = new JButton("add wigle scan");
		btnAddWigleScan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				File folder = new File(txtPathOfRawCsv.getText());
				try {
					if (folder.isDirectory() && folder.canRead()) {
						CsvFileProcessing file = new CsvFileProcessing(folder);
						DataBase tempData = new DataBase(file.getOutputName());
						dataBase.addAll(tempData);
						cleanFilter(filteredDataBase, dataBase);
						JOptionPane.showMessageDialog(null, "added all wifi samples from wigle folder to database!");
					} else {
						JOptionPane.showMessageDialog(null, "bad path");
					}
				} catch (Exception e) {
					e.printStackTrace();

				}

			}
		});
		btnAddWigleScan.setBounds(29, 45, 162, 47);
		frame.getContentPane().add(btnAddWigleScan);

		txtPathOfRawCsv = new JTextField();
		txtPathOfRawCsv.setText("path of csv folder");
		txtPathOfRawCsv.setBounds(262, 45, 162, 47);
		frame.getContentPane().add(txtPathOfRawCsv);
		txtPathOfRawCsv.setColumns(10);

		JButton btnAddProcessedCsv = new JButton("add processed csv");
		btnAddProcessedCsv.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				File file = new File(txtPathOfProcessedCsv.getText());
				try {
					if (file.isFile() && file.canRead()) {
						DataBase tempData = new DataBase(file.getPath());
						dataBase.addAll(tempData);
						cleanFilter(filteredDataBase, dataBase);
						JOptionPane.showMessageDialog(null, "added all wifi samples from CSV File to database!");
					} else {
						JOptionPane.showMessageDialog(null, "bad path");
					}
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

				}
			}
		});
		btnAddProcessedCsv.setBounds(29, 115, 162, 47);
		frame.getContentPane().add(btnAddProcessedCsv);

		txtPathOfProcessedCsv = new JTextField();
		txtPathOfProcessedCsv.setText("path of processed csv file");
		txtPathOfProcessedCsv.setBounds(262, 115, 162, 47);
		frame.getContentPane().add(txtPathOfProcessedCsv);
		txtPathOfProcessedCsv.setColumns(10);

		button3 = new JButton("clear data");
		button3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dataBase.clear();
				cleanFilter(filteredDataBase, dataBase);
				JOptionPane.showMessageDialog(null, "filter was cleared");
				JOptionPane.showMessageDialog(null, "all data was cleared");
			}
		});
		button3.setBounds(397, 451, 162, 47);
		frame.getContentPane().add(button3);

		button4 = new JButton("save data as csv");
		button4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (filterStats.equals("")) {
						dataBase.WriteSamplesToFile("dataBaseOutput.csv");
					} else {
						filteredDataBase.getWifiSampleList().clear();
						filteredDataBase.addAll(dataBase.generateFilteredData());
						filteredDataBase.WriteSamplesToFile("dataBaseOutput.csv");
					}
					JOptionPane.showMessageDialog(null, "saved data as csv");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		button4.setBounds(29, 451, 162, 47);
		frame.getContentPane().add(button4);

		JButton button5 = new JButton("save data as kml");
		button5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (filterStats.equals("")) {
						ExportToKml exportKmlData = new ExportToKml(dataBase);
					} else {
						filteredDataBase.getWifiSampleList().clear();
						filteredDataBase.addAll(dataBase.generateFilteredData());
						ExportToKml exportKmlData = new ExportToKml(filteredDataBase);
					}
					JOptionPane.showMessageDialog(null, "saved data as kml");
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});
		button5.setBounds(211, 451, 171, 47);
		frame.getContentPane().add(button5);

		btnFilterByName = new JButton("filter by time");
		btnFilterByName.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				SampleFilter timeFilter = null;
				String operator = "";
				if (rdbtnNot.isSelected()) {
					operator += "not";
				}
				if (rdbtnOr.isSelected()) {
					operator += "or";
				} else if (rdbtnAnd.isSelected()) {
					operator += "and";
				}
				try {
					if (isFormatable(txtStartTime.getText()) && isFormatable(txtEndTime.getText())) {
						timeFilter = new TimeFilter(txtStartTime.getText(), txtEndTime.getText(), operator);
						dataBase.addFilter(timeFilter);
						// set
						filteredDataBase.getWifiSampleList().clear();
						filteredDataBase.addAll(dataBase.generateFilteredData());
						filterStats += timeFilter.toString();
						updateStats(filteredDataBase, lblStats);
						JOptionPane.showMessageDialog(null, "filtered by time");
					} else {
						JOptionPane.showMessageDialog(null, "bad format, can not filter by time");
					}
				} catch (ParseException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}

			private boolean isFormatable(String text) {
				String[] s = text.split(" ");
				if (s.length == 2) {
					String[] s2 = s[0].split("-");
					if (s2.length == 3) {
						s2 = s[1].split(":");
						if (s2.length == 3)
							return true;
					}
				}
				return false;
			}
		});
		btnFilterByName.setBounds(29, 185, 162, 47);
		frame.getContentPane().add(btnFilterByName);

		txtStartTime = new JTextField();
		txtStartTime.setText("start time");
		txtStartTime.setBounds(262, 185, 162, 46);
		frame.getContentPane().add(txtStartTime);
		txtStartTime.setColumns(10);

		txtEndTime = new JTextField();
		txtEndTime.setText("end time");
		txtEndTime.setToolTipText("");
		txtEndTime.setBounds(455, 185, 162, 46);
		frame.getContentPane().add(txtEndTime);
		txtEndTime.setColumns(10);

		btnFilterByLocation = new JButton("filter by location");
		btnFilterByLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (isNumeric(txtMinLat.getText(), txtMaxLat.getText(), txtMinLon.getText(), txtMaxLon.getText(),
						txtMinAlt.getText(), txtMaxAlt.getText())) {
					String operator = "";
					if (rdbtnNot.isSelected()) {
						operator += "not";
					}
					if (rdbtnOr.isSelected()) {
						operator += "or";
					} else if (rdbtnAnd.isSelected()) {
						operator += "and";
					}
					SampleFilter locationFilter = new LocationFilterByRange(txtMinLat.getText(), txtMinLon.getText(),
							txtMinAlt.getText(), txtMaxLat.getText(), txtMaxLon.getText(), txtMaxAlt.getText(),
							operator);
					dataBase.addFilter(locationFilter);
					JOptionPane.showMessageDialog(null, "filtered by location");
					// set
					filteredDataBase.getWifiSampleList().clear();
					filteredDataBase.addAll(dataBase.generateFilteredData());
					filterStats += locationFilter.toString();
					updateStats(filteredDataBase, lblStats);
				} else {
					JOptionPane.showMessageDialog(null, "bad format, could not filter by location");
				}
			}

			public boolean isNumeric(String string, String string1, String string2, String string3, String string4,
					String string5) {
				return string.matches("-?\\d+(\\.\\d+)?") && string1.matches("-?\\d+(\\.\\d+)?")
						&& string2.matches("-?\\d+(\\.\\d+)?") && string3.matches("-?\\d+(\\.\\d+)?")
						&& string4.matches("-?\\d+(\\.\\d+)?") && string5.matches("-?\\d+(\\.\\d+)?");
			}
		});
		btnFilterByLocation.setBounds(29, 253, 162, 46);
		frame.getContentPane().add(btnFilterByLocation);

		JButton btnFilterByPhoneId = new JButton("filter by phone id");
		btnFilterByPhoneId.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String operator = "";
				if (rdbtnNot.isSelected()) {
					operator += "not";
				}
				if (rdbtnOr.isSelected()) {
					operator += "or";
				} else if (rdbtnAnd.isSelected()) {
					operator += "and";
				}

				SampleFilter phoneIdFilter = new PhoneIdFilter(txtPhoneId.getText(), operator);
				dataBase.addFilter(phoneIdFilter);
				JOptionPane.showMessageDialog(null, "filtered by device");
				// set
				filteredDataBase.getWifiSampleList().clear();
				filteredDataBase.addAll(dataBase.generateFilteredData());
				filterStats += phoneIdFilter.toString();
				updateStats(filteredDataBase, lblStats);
			}
		});
		btnFilterByPhoneId.setBounds(29, 321, 162, 46);
		frame.getContentPane().add(btnFilterByPhoneId);

		txtMinLat = new JTextField();
		txtMinLat.setText("min lat");
		txtMinLat.setBounds(262, 253, 123, 16);
		frame.getContentPane().add(txtMinLat);
		txtMinLat.setColumns(10);

		txtPhoneId = new JTextField();
		txtPhoneId.setText("phone id");
		txtPhoneId.setBounds(262, 322, 162, 45);
		frame.getContentPane().add(txtPhoneId);
		txtPhoneId.setColumns(10);

		txtMaxLat = new JTextField();
		txtMaxLat.setText("max lat");
		txtMaxLat.setColumns(10);
		txtMaxLat.setBounds(262, 282, 123, 16);
		frame.getContentPane().add(txtMaxLat);

		txtMinLon = new JTextField();
		txtMinLon.setText("min lon");
		txtMinLon.setColumns(10);
		txtMinLon.setBounds(397, 250, 123, 16);
		frame.getContentPane().add(txtMinLon);

		txtMaxLon = new JTextField();
		txtMaxLon.setText("max lon");
		txtMaxLon.setColumns(10);
		txtMaxLon.setBounds(397, 283, 123, 16);
		frame.getContentPane().add(txtMaxLon);

		txtMaxAlt = new JTextField();
		txtMaxAlt.setText("max alt");
		txtMaxAlt.setColumns(10);
		txtMaxAlt.setBounds(532, 283, 123, 16);
		frame.getContentPane().add(txtMaxAlt);

		txtMinAlt = new JTextField();
		txtMinAlt.setText("min alt");
		txtMinAlt.setColumns(10);
		txtMinAlt.setBounds(532, 250, 123, 16);
		frame.getContentPane().add(txtMinAlt);

		rdbtnAnd = new JRadioButton("and");
		rdbtnAnd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});

		rdbtnAnd.setBounds(95, 398, 65, 25);
		frame.getContentPane().add(rdbtnAnd);

		rdbtnOr = new JRadioButton("or");
		rdbtnOr.setSelected(true);
		rdbtnOr.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		rdbtnOr.setBounds(39, 398, 41, 25);
		frame.getContentPane().add(rdbtnOr);

		rdbtnNot = new JRadioButton("not");
		rdbtnNot.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

			}
		});
		rdbtnNot.setBounds(175, 398, 65, 25);
		frame.getContentPane().add(rdbtnNot);

		btnClearFilter = new JButton("clear filter");
		btnClearFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cleanFilter(filteredDataBase, dataBase);
				JOptionPane.showMessageDialog(null, "filter was cleared");
			}
		});
		btnClearFilter.setBounds(574, 451, 162, 47);
		frame.getContentPane().add(btnClearFilter);

		JLabel lblFilterStats = new JLabel("filter stats:");
		lblFilterStats.setBounds(262, 402, 74, 16);
		frame.getContentPane().add(lblFilterStats);

		btnAddFilter = new JButton("add filter");
		btnAddFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					File file = new File(txtPathOfFilter.getText());
					dataBase.getFilterList().clear();
					dataBase.loadFilter(txtPathOfFilter.getText());

					// set
					filteredDataBase.getWifiSampleList().clear();
					filteredDataBase.addAll(dataBase.generateFilteredData());
					filterStats = dataBase.filterListToString();
					updateStats(filteredDataBase, lblStats);

					if (file.isFile() && file.canRead()) {
						JOptionPane.showMessageDialog(null, "filter was added from file");
					} else {
						JOptionPane.showMessageDialog(null, "failed to add filter from file");
					}

				} catch (ClassNotFoundException | IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnAddFilter.setBounds(667, 45, 162, 47);
		frame.getContentPane().add(btnAddFilter);

		txtPathOfFilter = new JTextField();
		txtPathOfFilter.setText("path of filter file");
		txtPathOfFilter.setBounds(841, 45, 162, 47);
		frame.getContentPane().add(txtPathOfFilter);
		txtPathOfFilter.setColumns(10);

		JLabel lbldefultAsOr = new JLabel("(default as or operator)");
		lbldefultAsOr.setBounds(49, 422, 147, 16);
		frame.getContentPane().add(lbldefultAsOr);

		btnFindMyLocation = new JButton("find my location algo1");
		btnFindMyLocation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtMac.getText().split(":").length > 3) {
					if (filterStats.equals("")) {
						try {
							JOptionPane.showMessageDialog(null, dataBase.findMyLocation(txtMac.getText()));
						} catch (HeadlessException | ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						try {
							JOptionPane.showMessageDialog(null, filteredDataBase.findMyLocation(txtMac.getText()));
						} catch (HeadlessException | ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "bad format, could not find location");
				}

			}
		});
		btnFindMyLocation.setBounds(667, 115, 162, 45);
		frame.getContentPane().add(btnFindMyLocation);

		txtMac = new JTextField();
		txtMac.setText("mac");
		txtMac.setBounds(841, 116, 162, 45);
		frame.getContentPane().add(txtMac);
		txtMac.setColumns(10);

		btnFindMyLocation_1 = new JButton("find my location algo2");
		btnFindMyLocation_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtalgo2op1.getText().split(" ").length == 6) {
					String arr[] = txtalgo2op1.getText().split(" ");
					if (filterStats.equals("")) {
						try {

							JOptionPane.showMessageDialog(null,
									dataBase.findMyLocation2(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5]));
						} catch (HeadlessException | ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						try {
							JOptionPane.showMessageDialog(null, filteredDataBase.findMyLocation2(
									dataBase.findMyLocation2(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5])));
						} catch (HeadlessException | ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "bad format, could not find location");

				}
			}
		});
		btnFindMyLocation_1.setBounds(667, 185, 162, 46);
		frame.getContentPane().add(btnFindMyLocation_1);

		txtalgo2op1 = new JTextField();
		txtalgo2op1.setText("mac1.. mac3 signal1.. signal3");
		txtalgo2op1.setBounds(841, 185, 187, 46);
		frame.getContentPane().add(txtalgo2op1);
		txtalgo2op1.setColumns(10);

		btnFindMyLocation_2 = new JButton("find my location algo2");
		btnFindMyLocation_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (txtalgo2op2.getText().split(",").length > 8) {
					if (filterStats.equals("")) {
						try {
							JOptionPane.showMessageDialog(null, dataBase.findMyLocation2(txtalgo2op2.getText()));
						} catch (HeadlessException | ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					} else {
						try {
							JOptionPane.showMessageDialog(null,
									filteredDataBase.findMyLocation2(txtalgo2op2.getText()));
						} catch (HeadlessException | ParseException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				} else {
					JOptionPane.showMessageDialog(null, "bad format, could not find location");
				}

			}
		});
		btnFindMyLocation_2.setBounds(667, 253, 162, 46);
		frame.getContentPane().add(btnFindMyLocation_2);

		txtalgo2op2 = new JTextField();
		txtalgo2op2.setText("wifi sample in String format");
		txtalgo2op2.setBounds(841, 253, 187, 46);
		frame.getContentPane().add(txtalgo2op2);
		txtalgo2op2.setColumns(10);

		lblNewLabel = new JLabel("see Test file for example");
		lblNewLabel.setBounds(841, 302, 187, 16);
		frame.getContentPane().add(lblNewLabel);

		btnSaveFilter = new JButton("save filter");
		btnSaveFilter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					dataBase.saveFilter();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSaveFilter.setBounds(748, 451, 155, 47);
		frame.getContentPane().add(btnSaveFilter);

		JButton btnLoadFromServer = new JButton("load from server");
		btnLoadFromServer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				MySql serverData = new MySql(txtIp.getText(), txtUrl.getText(), txtUser.getText(),
						txtPassword.getText());
				dataBase.addAll(serverData.getMySqlData().getWifiSampleList());
				cleanFilter(filteredDataBase, dataBase);
				JOptionPane.showMessageDialog(null, "added all wifi samples from MySql Server to database!");
			}
		});
		btnLoadFromServer.setBounds(397, 393, 155, 45);
		frame.getContentPane().add(btnLoadFromServer);

		txtIp = new JTextField();
		txtIp.setText("ip");
		txtIp.setBounds(574, 393, 116, 16);
		frame.getContentPane().add(txtIp);
		txtIp.setColumns(10);

		txtUrl = new JTextField();
		txtUrl.setText("url");
		txtUrl.setBounds(574, 422, 116, 16);
		frame.getContentPane().add(txtUrl);
		txtUrl.setColumns(10);

		txtUser = new JTextField();
		txtUser.setText("user");
		txtUser.setBounds(713, 393, 116, 16);
		frame.getContentPane().add(txtUser);
		txtUser.setColumns(10);

		txtPassword = new JTextField();
		txtPassword.setText("password");
		txtPassword.setBounds(713, 419, 116, 16);
		frame.getContentPane().add(txtPassword);
		txtPassword.setColumns(10);

	}

	public void cleanFilter(DataBase filteredDataBase, DataBase dataBase) {
		filterStats = "";
		filteredDataBase.clear();
		dataBase.getFilterList().clear();
		updateStats(dataBase, lblStats);
	}

	public static void updateStats(DataBase dataBase, JLabel lblStats) {
		lblStats.setText("stats:" + " number of samples = " + dataBase.size() + " number of mac addreass: "
				+ dataBase.getNumberOfMacs() + filterStats);
	}
}
