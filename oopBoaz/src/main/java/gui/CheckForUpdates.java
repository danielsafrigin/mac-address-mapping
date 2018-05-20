package main.java.gui;

import java.io.File;

import main.java.dataObjects.DataBase;

public class CheckForUpdates extends Thread {
	private String csvPath;
	private String WiglePath;
	private long wigleLastModified[];
	private long csvLastModified[];
	private DataBase dataBase;
	private DataBase filteredDataBase;

	public CheckForUpdates(DataBase dataBase, DataBase filteredDataBase) {
		this.WiglePath = "dataBase\\wigleData\\data";
		this.csvPath = "dataBase\\proccesedData";
		this.wigleLastModified = initializeLastModifiedArray(WiglePath);
		this.csvLastModified = initializeLastModifiedArray(csvPath);
		this.dataBase = dataBase;
		this.filteredDataBase = filteredDataBase;
	}

	/**
	 * initialize times of last changes in files
	 * 
	 * @param path
	 * @return
	 */
	private long[] initializeLastModifiedArray(String path) {
		File directory = new File(path);
		File[] files = directory.listFiles();
		long[] lastModifiedArr = new long[files.length];
		for (int i = 0; i < files.length; i++) {
			lastModifiedArr[i] = files[i].lastModified();
		}
		return lastModifiedArr;
	}

	/**
	 * check for changes, if found reload Data base
	 */
	public void run() {
		while (true) {

			File wigleDirectory = new File(WiglePath);
			File[] wigleFiles = wigleDirectory.listFiles();
			File csvDirectory = new File(csvPath);
			File[] csvFiles = csvDirectory.listFiles();
			if (wigleLastModified.length != wigleFiles.length || csvLastModified.length != csvFiles.length) {
				try {
					filteredDataBase.clear();
					dataBase.reload();
					this.wigleLastModified = initializeLastModifiedArray(WiglePath);
					this.csvLastModified = initializeLastModifiedArray(csvPath);
					Gui.filterStats = "";
					Gui.updateStats(dataBase, Gui.lblStats);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				for (int i = 0; i < wigleFiles.length; i++) {
					if (this.wigleLastModified[i] != wigleFiles[i].lastModified()) {
						try {
							filteredDataBase.clear();
							dataBase.reload();
							this.wigleLastModified = initializeLastModifiedArray(WiglePath);
							Gui.filterStats = "";
							Gui.updateStats(dataBase, Gui.lblStats);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				for (int i = 0; i < csvFiles.length; i++) {
					if (this.csvLastModified[i] != csvFiles[i].lastModified()) {
						try {
							filteredDataBase.clear();
							dataBase.reload();
							this.csvLastModified = initializeLastModifiedArray(csvPath);
							Gui.filterStats = "";
							Gui.updateStats(dataBase, Gui.lblStats);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

	}

}
