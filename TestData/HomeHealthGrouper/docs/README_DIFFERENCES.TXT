Home Health Grouper
Development Difference Notes


Table of Contents

Changes from version dated 7/29/09 to 8/4/09
Changes from version dated 8/4/09 to 9/16/09
Changes from version dated 9/16/09 to 10/26/09
Changes from version dated 10/26/09 to 11/16/09
Changes from version dated 11/16/09 to 11/19/09
Changes from version V3110 (dated 11/16/2009) to V3210 Beta (dated 05/2010)
Changes from version V3210 Beta (dated 05/26/2010) to V3210 October (dated 7/2010)
Changes from version V3210 October (dated 07/23/2010) to V3211 (dated 7/2011)
Changes from version V3211 to V3312 January 2012 (dated 11/2011)
Changes from version V3212 to V3413 January 2013 (dated 10/2012)
Changes from version V3413 to v3414 January 2014 (dated 10/2013)
Changes from version V3414 to v3514 October 2014 (dated 08/2014)
Changes from version V3514 to v4115 January 2015 (dated 11/2014)
Changes from version V4115 to v5115 October 2015 (dated 03/2015)
Changes from version V5115 to v5116 January 2016 (dated 11/2015)
Changes from version V5116 to v5216 October 2016 (dated 06/2016)
Changes from version V5216 to v6117 January 2017 (dated 11/2016)
Changes from version V6117 to v6117.R1 January 2017 (dated 11/2016)
Changes from version V6117.R1 to v6217 October 2017 (dated 06/2017)
Changes from version V6217 to v7118 January 2018 (dated 11/2017)
Changes from version V7118 to v7218 October 2018 (dated 06/2018)
Changes from version V7218 to v8119 January 2019 (dated 11/2019)
Changes from version v8119 to v8119.R1 January 2019 (dated 11/2019)
Changes from version v8119 to v8219 October 2019 (dated 06/2019)

Changes from version dated 7/29/09 to 8/4/09

Determining the Grouper version to score with
	In the 7/29/09 jar, the base Grouper version class GrouperVersion_v2308_1 allows for a valid Start and Thru dates for when a version is usable. It also allows a Window date (a date several days prior to the Start date) for follow up assessments only, i.e. those assessments with reasons 4 or 5.  However, not all versions allow windows for such assessments, and in this case the Window date would be set to null in the constructor.  In this version of the software, the constructor on line 111 of file com.mmm.cms.homehealth.v2308_1.GrouperVersion_v2308_1 incorrectly used the Thru when the Window date was null.  So, follow-up assessments of 4 or 5 would incorrectly not find their appropriate Grouper to score with, unless the completion date (M090) was actually on the Thru date.

Scoring Clinically for Group 5
	Diagnosis Category 5 would incorrectly score if the Diagnosis Category 12 was on the record OR if the Therapy Ent Nutrition = 1.  The Diagnosis Category 5 scoring has been changed to score when Diagnosis Category 12 is on the record AND if the Therapy Ent Nutrition = 1.

Validation of Gangrene Exclusions
	Diagnosis code 785.4, representing Gangrene, has an associated list of diagnosis codes that can not be next on the record. In the 7/29/09 jar version, the secondary code is checked to determine if it is on the exclusion list. If it is, then the code is excluded.  However, no check was made to determine if the secondary code was valid.  The 8/4/09 jar version performs this valid code check on the secondary code.




Changes from version dated 8/4/09 to 9/16/09

Added OASIS-C record format handling
	In preparation for the OASIS-C record format usage on January 1, 2010, this version provides additions that read the OASIS-C record, convert it to the internal Home Health Record, validate it, and score it. The scoring, in order to maintain the same logic as the prior version, maps the OASIS-C values backwards to OASIS-B values, and performs the scoring based on the latest OASIS-B scoring logic.

Changed Record Field Names
	With the OASIS-B and OASIS-C record formats being similar, it is appropriate to treat many fields in a similar fashion.  However, some of the fields with the same representation and use changed their field name number and/or their location within the record.  To reduce the amount of programming, the field name numbers were removed. For example, the OASIS-B field named M0440_LESION_OPEN_WND has been renamed in OASIS-C as M1350_LESION_OPEN_WND.  Both of these fields are described as "Has Skin Lesion Or Open Wound".  Therefore, the "M#" part of the field name is no longer referenced within the Java source code.  Instead the field is referred to as LESION_OPEN_WND when dealing with either the OASIS-B or OASIS-C record format.




Changes from version dated 9/16/09 to 10/26/09

NRS Validation based on Converted OASIS-C record
	The NRS Validation of the record, occurring after the Clinical Validation and scoring of the record, was incorrectly using a form of the record which was mapped from OASIS-C values to OASIS-B values.  This would manifest itself when any records flagged as invalid for the Number of Stasis Ulcers during the Clinical Validation, would not be similarly flagged as invalid for the NRS Validation, and therefore would score higher than expected for the NRS portion of the HIPPS code.  This has been corrected so that the NRS Validation is performed on the OASIS-C formatted data, prior to mapping to OASIS-B format.

Example testing applications continue processing
	Some of the example testing modules supplied with the Grouper distribution package would stop processing a batch of records if the module was to show record scoring details and the HHRG could not find an appropriate Grouper version to score with.  Records that do not match an appropriate Grouper are now skipped and the processing continues.

Validate that the Version_CD1 field matches with the Completion Date 
	If the record contains a "C" in the first position of Version CD1 field, then the Date Assessment Completed must be on or after 1/1/2010 or the HIPPS code will not be generated.  If the record contains a "B" in the first position of in the Version CD1 field, then the Date Assessment Completed must be on or before 12/31/2009 or the HIPPS code will not be generated. 

Additional edits to OASIS-C record
	The following edits were added to the Pressure Ulcer fields in order to be more complete with the OASIS-C edits. However, these edits are on fields that do not have an impact on scoring, but are directly related to field that do effect scoring.  The edits are:

      1) NSTG_DRSG_SOC_ROC must be <= NSTG_DRSG. 
      
      2) NSTG_CVRG_SOC_ROC must be <= NSTG_CVRG. 
      
      3) NSTG_DEEP_TISUE_SOC_ROC must be <= NSTG_DEEP_TISUE. 

	Due to the change in values of the Stasis Ulcer Present field, an edit was created to validate the Unobserved Stasis Ulcer as part of the new Stasis Ulcer edits.

      1) If STAS_ULCR_PRSNT == 00 or 03, then NUM_STAS_ULCR and STUS_PRBLM_STAS_ULCR must be skipped (blank).
     
      2) If STAS_ULCR_PRSNT == 01 or 02, then NUM_STAS_ULCR and STUS_PRBLM_STAS_ULCR cannot be (blank).


Additional edits to OASIS-C record due to specification updates
	The following additional edits on the OASIS-C record will result in a Clinical Issue Flag indicator when the edits fail.  

      1) If  NBR_PRSULC_STG2, NBR_PRSULC_STG3, NBR_PRSULC_STG4 and NSTG_CVRG are all = 0 (zero), then STUS_PRBLM_PRSR_ULCR must equal NA.

      2) If NBR_PRSULC_STG2 is greater than 0 (zero) AND NBR_PRSULC_STG3, NBR_PRSULC_STG4, and NSTG_CVRG are all = 0 (zero) then STUS_PRBLM_PRSR_ULCR must equal 03.

      3) If NBR_PRSULC_STG3 or NBR_PRSULC_STG4 is greater than 0 (zero), then STUS_PRBLM_PRSR_ULCR must equal 00, 01, 02, or 03.
      
      4) If NSTG_CVRG is greater than 0 (zero) and NBR_PRSULC_STG2, NBR_PRSULC_STG3, and NBR_PRSULC_STG4 are all = 0 (zero) then STUS_PRBLM_PRSR_ULCR must equal 02 or 03

      5) Under NBR_PRSULC_STG3 and NBR_PRSULC_STG4, if NBR_PRSULC_STG3, NBR_PRSULC_STG4, or NSTG_CVRG are greater than 0 (zero), then PRSR_ULCR_LNGTH, PRSR_ULCR_WDTH, and PRSR_ULCR_DPTH cannot be blank.

      6) Under NBR_PRSULC_STG3 and NBR_PRSULC_STG4, if NBR_PRSULC_STG3, NBR_PRSULC_STG4, and NSTG_CVRG are equal to 0 (zero), then PRSR_ULCR_LNGTH, PRSR_ULCR_WDTH, and PRSR_ULCR_DPTH must be blank.


OASIS-C mapping of Status of Most Problematic Stasis Ulcer to OASIS-B
	Status of Most Problematic Stasis Ulcer = 00, Newly Epithelialized, under OASIS-C mapped back to 01 - Fully Granulating under OASIS-B.  01 scores points but 00 is not valid under OASIS-B.  It makes more sense to map OASIS-C 01 to OASIS-B NA, since NA is a non-point scoring value.


Test Data
	Due to source code changes, some of the supplied TestData files have been updated with the latest Grouping Results. When no HIPPS code is generated, all of the output values will be blank.




Changes from version dated 10/26/09 to 11/16/09

Default HomeHealthGrouper.properties location
	When the home_health_config environment variable is not set, the default directory for HomeHealthGrouper.properties file was originally set to C:\Program Files\HomeHealthGrouper\config.  This has been changed to first check the "current directory"\config.  If the file does not exist there, then it uses the previous default directory of C:\Program Files\HomeHealthGrouper\config.

Changes to "at least one" indicator for Pressure Ulcers
	The validation routine validateNPRSULC() in the AbstractBaseValidator_v3110 class, lines 525 thru 545 check to determine if at least one Stage indicator was > 0 or any other item is > 0, for when the UNHLD_STG2_PRSR_ULCR = 1.  The check incorrectly checked for all values being less than 0 instead of all values being equal to 0 (the string value actually being equal to "00").  This has been corrected.

Moved GrouperDataManager_v2308 to a more appropriate package
	The class GrouperDataManager_v2308 is used to load the data files, but it is a generic loader and its name and original location are misleading. This has been moved to the package com.mmm.cms.homehealth, and renamed to GrouperDataManager.java

Stricter RFA checking for validations
	Previously, when checking M-field "A" that was valid for RFAs 1-5, and its validation includes M-field "B", the validation was performed on "B" even though "B" was only active for RFAs 1-3. These validations have been made stricter such that the validation is only performed for the least common RFAs for the fields involved.

Missing Primary Diagnosis prevents Diagnosis related scoring
	Previously, the missing Primary Diagnosis would only generate a Manifestation flag.  It will now also generate a clinical flag, and ensure that no other diagnosis codes are set to not valid for scoring.

Error in validating blank Pressure Ulcer fields
	The check for this excluded the value of 0 when checking that the fields were blank when the Unhealed Stage 2 Ulcer was set to 0. This has been corrected.

Ensure contiguous listing of Diagnosis Codes in Column 1
	If there is a blank entry in column 1, then the following codes in column1 should also be blank.  This includes even the primary diagnosis.  Any codes that follow the blank entry will be excluded from scoring on the record, and the clinical flag will be set.
 
Ensure no duplicate Diagnosis Codes in Column 1
	If a code is duplicated in column 1, then the first instance of the code is allowed to score, but the subsequent instances of the code will not score.  A clinical flag is set when duplicates are found.

Changed the configuration files to use a single file
	Due to the flexibility of the HHRG configuration, there is a set of configuration files that determine where the supporting data is located.  This configuration can now be consolidated into a single configuration file, making custom installation easier to manage.  However, the previous configuration information has been left intact for those who have created custom installs, but do not want to, at this time, make any changes to those installs - i.e. the previous configuration options will continue to work.  The new configuration options should help any subsequent custom install processes to be easier.  The new configuration is the preferred setup for the HHRG, and in subsequent deliveries, it will become the default setup.  The old set up is being deprecated.

Removal of NSTG_CVRG for validating NBR_PRSULC_STG3 and NBR_PRSULC_STG3
	Under validateNPRSULC method, removed the inclusion of variable NSTG_CVRG when dealing with only Edits #3 and #4 in the UDS Specs for NBR_PRSULC_STG3 and NBR_PRSULC_STG4

No version or flag for no HIPPS code
	If a HIPPS code is unable to be generated, the Version and Flag fields were incorrectly populated on output.  This has been corrected.

Pressure Ulcer fields affecting the Unobserved Pressure Ulcer validity
	The grouper was incorrectly allowing NRS points to be scored for Unobservable Pressure Ulcer if other Pressure Ulcer fields were flagged as invalid.  This has been corrected.

Complete checking of UNHLD_STG2_PRSR_ULCR
	The grouper was not checking allowable values for UNHLD_STG2_PRSR_ULCR which was allowing points to score inadvertently.  This has been corrected.

Case sensitive data files
	On UNIX systems, file case lettering is important.  In the supporting table data directory, Grouper_v2308_2.tables, the following files have been corrected to have the same case as in the other table data directories: DiagnosisCodes.txt, DiagnosisEtiologyPairs.txt, NRSDiagnosisCodes.txt.  Corresponding filenames have been updated in the Java class  com.mmm.cms.homehealth.GrouperDataManager.



Changes from version dated 11/16/09 to 11/19/09

	These changes were due to the Errata published by CMS that clarifies the OASIS-C specification.

Added Non-Staging Deep Tissue checks to Pressure Ulcer validation
	Checks for the value of NonStaging Deep Tissue value which was added to the validation when the Unhealed Stage 2 Pressure Ulcer = 1

Added Non-Staging due to Coverage added
	For Stage 3 and Stage 4 Pressure Ulcers, the validation now includes checks for Non-Staging due to coverage which is now consistent with the Non-Staging due to Coverage validation. 

Removed check for blank rows for Diagnosis Codes
	The Errata explains that the State system will adjust the Diagnosis codes when a blank row is followed by a non-blank row.  Such cases previously were fatal errors not allowing scoring, but now, scoring will continue, although, manifestation/etiology pairs not paired one row after the other will still not score properly.

Add Incontinence Timing for RFA 1 & 3
	Additional checks are performed on Urinary Incontinence with regard to the timing, however, the Errata provides that this check need only be done when the RFA is 1 or 3.



Changes from version v3110 (January 2010 release - dated 11/16/2009) to v3210 Beta (dated 05/26/2010)


Reorganization of logic for scoring Diagnosis codes 
	Previous versions scored the Diagnosis codes with a multi-pass process.  This version uses a single pass process resulting in Java code that is easier to read.

Corrects ordering in manifestation/etiology contention 
	Previous version had issues when there were multiple contention pairs whether or not the record was using V-codes.  The order of the codes within the record affected the resulting score.  This version eliminates the ordering issue by first scoring all non-contention codes, and then scoring all contention codes.  Ordering of codes by moving it in and out of the Primary Diagnosis field may still affect the score in some cases since the Primary Diagnosis field is scored higher in those cases.

More detailed reporting
	The reporting mechanism during scoring shows the exact row and value within the Casemix Adjustment table that is applied to each item scored as well as identify those Diagnosis codes that do not score, and why.

Source code remains in zip file
	Previous versions uncompressed the source into its own directory when installing.  Now the sources will remain in the zip file.  The user may uncompress them as needed. Java docs remain in zip file. Previous versions uncompressed the Java docs into their own directory structure when installing.  Now the Java docs will remain in the zip file.  The user may uncompress them as needed.



Changes from version v3210 Beta (dated 05/26/2010) to V3210 (October 2010 release - dated 07/23/2010)

Addition of Home Health Sum of Chgs_100110_rev1.pdf 
	Document which reflects ICD-9-CM Code Changes for October 1, 2010.

Addition of Test Data file named TestDataV3210.txt (beta test cases)
	Additional test data file which includes data associated with codes effective 10/01/10.



Changes from version V3210 (October 2010 release - dated 07/23/2010) to V3211 (October 2011 release - dated 7/13/2011)

Addition of Home Health Summary of Changes_v3211_100111.pdf 
	Document which reflects ICD-9-CM Code Changes for October 1, 2011.

Addition of Test Data file named TestDataV3211.txt
	Additional test data file which includes data associated with codes effective 10/01/11.



Changes from version V3211 to V3312 January 2012 (dated 11/04/2011)

Additions of version V3312 Grouper engine and related I-9 codes (see Home Health Summary of Changes document).  Due to the I9 code changes, the Scoring_CasemixAdjustments.txt table has also been modified.

New logic for single Etiology and multiple Manifestation combinations based on the following: (note - this logic begins with V3312 and is not retroactive)
 	Etiology/manifestation pairings will be retained but the HHRG, when encountering an I-9 code listed under a manifestation/etiology edit, will look at multiple preceding I-9 fields to check for validation based upon the CMS proprietary eti/mani lists. An etiology must be sequenced prior to the manifestation code for the manifestation to be considered valid for scoring but not immediately preceding. There is neither a change to the pairings of etiology and manifestation nor a change to the etiology/manifestation logic in the payment diagnosis fields (col 3 and 4) as a result of this revision. The first valid preceding etiology code encountered by the HHRG is considered the manifestation code partner for scoring purposes (i.e only one of the etiology/manifestation may score).
	In the case of Gangrene, 785.4, the logic will look at the first preceding non-Manifestation code to create a pair used to determine whether or not those 2 codes allow the Gangrene to score points.  The logic for scoring gangrene when a V-code is in the first position has not changed.

Updated the Grouper tables by combining the ICD-9 codes table with the Optional V-Codes table. V-Codes are now referred to in the Java code as OptionalPayment codes, and are marked in the DiagnosisCodes table with "P" at the end of the row defining the code. The Etiology Pairs table now includes an indicator for "Exclusions", with the default being "Inclusion". This removes hardcoding of Gangrene in the Java code.

Table name change for all versions of the grouper tables.  The DiagnosticCategory.txt and NRSDiagnosticCategory.txt have been changed to DiagnosisGroup.txt and NRSDiagnosisGroup.txt respectively.

Addition of Test Data file named TestDataV3312.txt
	Additional test data file which includes data associated with codes effective 01/01/2012 and other changes.




Changes from version V3212 to V3413 January 2013 (dated 10/26/2012)

Additions of version V3413 Grouper engine and related I-9 codes (see Home Health Summary of Changes document).

New logic for Payment V-code pairing with specific diagnosis codes.

New logic for promoting diagnosis code(s) in position 2 (or Etiology/Manifestation pair in position 2 and 3) when position 1 contains a pairable V-code.

Corrected a Clinical Flag issue reporting that was previously unreported.

Added more detailed reporting of invalid codes during the record validation process.

Addition of Test Data file named TestDataV3413.txt
	Additional test data file which includes data associated with codes effective 01/01/2013 and other changes.




Changes from version V3413 to v3414 January 2014 (dated 10/09/2013)

Additions of version V3414 Grouper engine and related I-9 codes (see Home Health Summary of Changes document).

Java source code updates:
	Designed and compiled to Java 1.6
	Replaced Hashtable with HashMap
	Added @Override to methods
	Removed variable assignments that are not used.
	Removed unused imports
	Converted logging statements to use template logging
	Converted if(“string”.equals(“string”)) to use local final string
	Replaced StringBuffer with StringBuilder
	Removed unused package: com.mmm.cms.homehealth.proto.logic
	Replaced list “size() == 0” with “isEmpty()”
	Changed the following HomeHealthRecordIF’s get/set methods names:
		getFREQ_PAIN		to	getPAIN_FREQ_ACTVTY_MVMT
		setFREQ_PAIN		to	setPAIN_FREQ_ACTVTY_MVMT
		getNSTG_DEEP_TISSUE	to	getNSTG_DEEP_TISUE
		setNSTG_DEEP_TISSUE	to	setNSTG_DEEP_TISUE
		getSTASIS_ULCER		to	getSTAS_ULCR_PRSNT
		setSTASIS_ULCER		to	setSTAS_ULCR_PRSNT
		getNBR_STASULC		to	getNUM_STAS_ULCR
		setNBR_STASULC		to	setNUM_STAS_ULCR
		getSTAT_PRB_STASULC	to	getSTUS_PRBLM_STAS_ULCR
		setSTAT_PRB_STASULC	to	set STUS_PRBLM_STAS_ULCR
		getSURG_WOUND		to	getSRGCL_WND_PRSNT
		setSURG_WOUND		to	setSRGCL_WND_PRSNT
		getSTAT_PRB_SURGWND	to	getSTUS_PRBLM_SRGCL_WND
		setSTAT_PRB_SURGWND	to	setSTUS_PRBLM_SRGCL_WND
		getCUR_BATHING		to	getCRNT_BATHG
		setCUR_BATHING		to	setCRNT_BATHG
		getCUR_TOILETING	to	getCUR_TOILTG
		setCUR_TOILETING	to	setCUR_TOILTG
		getCUR_TRANSFERRING	to	getCUR_TRNSFRNG
		setCUR_TRANSFERRING	to	setCUR_TRNSFRNG
		getCUR_AMBULATION	to	getCRNT_AMBLTN
		setCUR_AMBULATION	to	setCRNT_AMBLTN
		getCUR_INJECT_MEDS	to	getCRNT_MGMT_INJCTN_MDCTN
		setCUR_INJECT_MEDS	to	setCRNT_MGMT_INJCTN_MDCTN

Logic updates:
	New logic change to remove the OASIS-C scoring dependency on OASIS-B scoring since OASIS-B processing is being deprecated

Configuration updates:
	New configuration change to remove the OASIS-B related version from being loaded by default.  The user can change this in the config file, config\HomeHealthGrouper.properties
	Within the NRSDiagnosisCodes.txt, removed the "U" and "D" indicators since they are redundant with the DiagnosticGroup indicator.

Test Data:
	Addition of Test Data file named TestDataV3414.txt, which includes data associated with codes effective 01/01/2014 and other changes.



Changes from version V3414 to v3514 October 2014 (dated 08/2014)
No changes to data or logic

Test Data:
	Addition of Test Data file named TestDataV3514.txt



Changes from version V3514 to v4115 January 2015 (dated 11/2014)
Logic Changes:
	Added check for manifestation in PDX based on the Medicare Code Editor (MCE) manifestation list. This will stop the record from scoring
	Added use of V-API for reporting validation edits
	Incorporated OASIS-C1 edit numbers for OASIS-C1 records
	Added ability to read OASIS-C 2.10 flat file and XML
	Added ability to read OASIS-C1 2.11 flat file and XML

Configuration:
	Added ability to configure the valid dates of Grouper versions
	Added ability to configure the valid dates of Record format readers

Test Data:
	Addition of Test Data file named TestDataV4115.txt
	Added test files TestDataV4115_C1.xml and TestDataV4115_C_Extended.xml
	For the runGrouperHPTest.bat file added the ability to set the following Options (if you have any issue with these options, please review the source code 
	for classed named: com.mmm.cms.homehealth.test.HomeHealthGrouper_HP and  com.mmm.cms.homehealth.test.CommonTester) (these options are not available through
	the DLL interface) :
        config=<file name>.properties  Required. No default.
        input=<file name>  Required. No default.
        limit=<positive integer> Optional. Default = all records
        details=<record number> Optional. Default no details
        outputFile=<file name> Optional. Default is input file name with "_OUT", appended to the base name - before the last decimal.
        records=<first record number> Optional. Default all records
        records=<first record number>-<last recod number> Optional. For doing a range of records. Default all records
        basePath=[path to the parent of the folders with the 'Grouper...tables' data]



Changes from version V4115 to v5115 October 2015 (dated 03/2015)
Logic Changes:
	Removed Optional Payment logic because ICD-10 is more specific and such Optional codes are no longer required
	Added ICD-10 codes
	Added ability to read OASIS-C1 2.12 flat file and XML
	Added ability to read OASIS-C 2.00 flat file incorrectly reformatter formatted as OASIS 2.10
	Separated OASIS Record validation from HH-PPS validation.

Configuration:
	Added ability configure a different OASIS record validator
	Renamed the VUT_API.jar to HH_PPS_V_API.jar

Test Data:
	Addition of Test Data file named TestDataV5115.txt


Changes from version V5115 to v5116 January 2016 (dated 11/2015)
Logic Changes:
	Changed V5115 effective dates to Oct 1, 2015 thru Dec 31, 2015
	Added Encounter codes effective for V5115 and V5116

Configuration:
	NONE

TestData:
	Addition of Test Data file named TestDataV5116.txt

	
Changes from version V5116 to v5216 October 2016 (dated 06/2016)
Logic Changes:
	Changed V5215 effective dates to Oct 1, 2016 thru Dec 31, 2016
	Update ICD-10 codes (see "Summary of Changes" document)

Configuration:
	NONE

TestData:
	Addition of Test Data file named TestDataV5216.txt

Tables:
	The following column names were added according to this table:

1. DiagnosisCodes.txt (4 columns):
	Diagnosis
	Description
	DiagnosticGroup
	LogicFlag
	
2. DiagnosisEtiologyPairs.txt (3 columns):
	Diagnosis1
	Diagnosis2
	LogicFlag
	
3. DiagnosticGroup.txt (2 columns):
	DiagnosticGroup
	Description
	
4. NRSDiagnosisCodes.txt (4 columns):
	Diagnosis
	Description
	NRSDiagnosticGroup
	LogicFlag
	
5. NRSDiagnosticGroup.txt (2 columns):
	DiagnosticGroup
	Description
	
6. NRSScoring_CasemixAdjustments.txt (3 columns):
	ID
	Description
	Equation
	
7. Scoring_CasemixAdjustments.txt (6 columns):
	ID
	Description
	Equation1
	Equation2
	Equation3
	Equation4

	
Changes from version V5216 to v6117 January 2017 (dated 11/2016)
Logic Changes:
	Added ability to read OASIS-C2 2.20 flat file and XML
	Gangrene (I96) logic changed
	Updated content (see "Summary of Changes" document)

Configuration:
	NONE

TestData:
	Addition of Test Data file named TestDataV6117.txt
	
Changes from version V6117.R0 to v6117.R1 January 2017 (dated 11/2016)
Logic Corrections:
	Correction of logic for gangrene (I96) to score when exclusion present
	Correction of logic to score primary awarding DG 19 (Skin 1 - Traumatic wounds, burns and post-operative complications)
	Correction of logic for assignment of validity  flag for field M2001 (Drug Regimen Review)
	
TestData:
	Update of Test Data file named TestDataV6117.txt
	Update of XML files found in XML_V6117_TestData folder
	
Changes from version V6117 to v6217 October 2017 (dated 06/2017)
	Updated content (see "Summary of Changes" document)
	OASIS validation edit log messaging was updated with OASIS C v2.20 edit indexes and descriptions
	Added Test Data file named TestDataV6217.txt
	Added XML files found in XML_V6217_TestData folder
	
Changes from version V6217 to v7118 January 2018 (dated 11/2017)
	Updated content (see "Summary of Changes" document)
	OASIS validation edit log messaging was updated with OASIS C v2.21 edit indexes and descriptions
	Added Test Data file named TestDataV7118.txt
	Added XML files under XML_V7118_TestData folder

Changes from version V7118 to v7218 October 2018 (dated 06/2018)
	Updated content (see "Summary of Changes" document)
	Added Test Data file named TestDataV7218.txt
	Added XML files found in XML_V7218_TestData folder
	
Changes from version V7218 to v8119 January 2019 (dated 11/2018)
	Updated content (see "Summary of Changes" document)
	OASIS fields, validation edits and log messaging were updated with OASIS D v2.30 specifications
	Added Test Data file named TestDataV8119.txt
	Added XML files under XML_V8119_TestData folder

Changes from version v8119 to v8119.R1 January 2019 (dated 11/2018)
	Updated OASIS validation library SOURCE CODE with version 2.30.1 of OASIS-D
	Fixed XML formatted assessment Java NPE issue for non-required fields
	Updated the NRS table in the HH-PPS Overview document to reflect the field descriptions in the NRSScoring_CasemixAdjustments table
	
Changes from version v8119.R1 to v8219 October 2019 (dated 06/2019)
	Updated content (see "Summary of Changes" document)
	Added Test Data file named TestDataV8219.txt
	Added XML files found in XML_V8219_TestData folder
