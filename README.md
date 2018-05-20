# year2-sm1-oop
project for my oop course

## OOP Project
The main purpose of this project is to develop complex system that you need to keep working on.<br>
doing so makes you understand the importance of clean and good code.<br>
in the this project we build a system that allows you to analyze wifi information.<br>
for example your input is a file of wifi scans in your city, then you can filter the data or even export it to kml file.<br>
(files which google maps can read and transfer represent as dots)<br>

### Libraries:<br>
JAK - https://labs.micromata.de/projects/jak/download.html to write a KML file(to use in Google Earth).<br>
MySQL connectors - https://dev.mysql.com/downloads/connector/j/5.1.html to connect to SQL server.<br>
Window Builder \ Swing - https://www.eclipse.org/windowbuilder/download.php to create GUI.<br>

Instructions:<br>

### export:<br>

Genrate procceseed csv file by giveing a folder path containing wigle files (via CsvFileProcessing object).<br>
Genrate procceseed kml file by giveing a procceseed csv file path (via ExportToKml object).<br>

###data objects:<br>
data base - data base containing a list of samples and a list of filters wifi sample - sample containing time, lat, lon, alt, phone id and a list of wifi spots wifi spot - spot containing ssid, mac, freq and signal<br>

### filter:<br>
filter by radius - get list containing all wifi samples with in a sertion radius<br>
filter by location - get a list containing all wifi samples between a range of location values<br>
filter by time - get a list containing all wifi samples between a range of time<br>
filter by phone id - get a list containing all wifi samples with the exact phone id<br>

(for examples -> how to run package)

### Gradle Instructions:<br>
open cmd go to project folder, write build gradle and run gradle.<br>

### Gui Instructions:<br>

https://i.imgur.com/BIYVs2E.png