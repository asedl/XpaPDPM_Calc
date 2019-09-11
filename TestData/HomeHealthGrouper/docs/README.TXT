		-- Home Health Java  --
		  Distribution pack


This distribution pack includes all the source code, source documentation, system
design, and test data for Home Health Grouper (HH-PPS) version v2308-v8219.

The source code was designed to work with Java 1.7 and above due to its use of
generics, enums, etc

TO INSTALL THE HH-PPS:

The contents in this zip file should be unzipped into the folder:

	"C:\hhpps"
The unzipping will create a subfolder of "HomeHealthGrouper" where the application will reside.

An overview of the system design is in the following file:

	HH-PPS_java_code_design.pdf
	
Source code documentation is in the following file:

	javadoc.zip


*** IMPORTANT ***
If installed into any other directory than the one defined above, then the config files 
in following directories must be updated:

	\config

	
The directories contain the following items:

	\config - the configuration files for the Home Health Grouper Java version

	\dist - pre built Java jar file for use with applications.  This is purely for 
		convenience because the source files will compile as well.

	\docs - holds the documentation for the version

	\Grouper_v2308_1.tables - supporting tables for the Grouper that processes records
		between Jan 1, 2008 and Sept 30, 2008

	\Grouper_v2308_2.tables - supporting tables for the Grouper that processes records
		between Oct 1, 2008 and Sept 30, 2009

	\Grouper_v2409_1.tables - supporting tables for the Grouper that processes records
		between Oct 1, 2009 and Sept 30, 2010

	Note about v3110: 
		The Grouper_v2409_1 set of supporting tables is the same for both the V2409, and V3110. 
		Therefore, there is no separate table folder or set of tables for V3110. The HH-PPS
		configuration file identifies the V2409 files as the ones to use.  Refer to the 
		\config\HomeHealthGrouper.properties for more details.

	\Grouper_v3210_1.tables - supporting tables for the Grouper that processes records
		between Oct 1, 2010 and Sept 30, 2011

	\Grouper_v3211_1.tables - supporting tables for the Grouper that processes records
		between Oct 1, 2011 and Dec 31, 2011

	\Grouper_v3312_1.tables - supporting tables for the Grouper that processes records
		between Jan 1, 2012 and Dec 31, 2012
 
	\Grouper_v3413_1.tables - supporting tables for the Grouper that processes records
		between Jan 1, 2013 and Sept 30, 2013.  Although there is no longer the concept 
		of a default grouper (for an assessment that is entered beyond the end date for 
		the latest grouper), there is a grace period which extends to Dec 31, 2013.

	\Grouper_v3414_1.tables - supporting tables for the Grouper that processes records
		between Jan 1, 2014 and Sept 30, 2014

	\Grouper_v3514_1.tables - supporting tables for the Grouper that processes records
		between Oct 1, 2014 and Dec 31, 2014

	\Grouper_v4115_1.tables - supporting tables for the Grouper that processes records
		between Jan 1, 2015 and Sept 30, 2015

	\Grouper_v5115_1.tables - supporting tables for the Grouper that processes records
		between Oct 1, 2015 and Dec 31, 2015

	\Grouper_v5116_1.tables - supporting tables for the Grouper that processes records
		between Jan 1, 2016 and Sept 30, 2016
		
	\Grouper_v5216_1.tables - supporting tables for the Grouper that processes records
		between Oct 1, 2016 and Dec 31, 2016
		
	\Grouper_v6117_1.tables - supporting tables for the Grouper that processes records
		between Jan 1, 2017 and Sept 30, 2017

	\Grouper_v6217_1.tables - supporting tables for the Grouper that processes records
		between Oct 1, 2017 and Dec 31, 2017

	\Grouper_v7118_1.tables - supporting tables for the Grouper that processes records
		between Jan 1, 2018 and Sept 30, 2018

	\Grouper_v7218_1.tables - supporting tables for the Grouper that processes records
		between Oct 1, 2018 and Dec 31, 2018

	\Grouper_v8119_1.tables - supporting tables for the Grouper that processes records
		between Jan 1, 2019 and Sept 30, 2019

	\Grouper_v8219_1.tables - supporting tables for the Grouper that processes records
		between Oct 1, 2019 and Dec 31, 2019

	\javadoc.zip - HTML documentation of the Java source code (# see note below)

	\HH_PPS_Java_source.zip - the Java source code (# see note below)

	\TestData - OASIS B, and C v2.00 and 2.10, C1 v2.11 and v2.12, C2 v2.20 and v2.21, D v2.30.1
		style records used for testing the Grouper logic. Note that by default the HH-PPS
		is not configured to score OASIS B records. See configuration file for more details.
	

# - some folders do not contain any files.  Instead they only contain other folders.


