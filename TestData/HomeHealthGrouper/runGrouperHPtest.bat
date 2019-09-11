SETLOCAL

SET BASEDIR=C:\hhpps\HomeHealthGrouper
SET HH_CLASSPATH=.;%BASEDIR%\dist\HomeHealthJava.jar;.;%BASEDIR%\dist\HH_PPS_V_API.jar
rem SET JAVA_VERSION=-version:1.7
SET TEST_FILE=%BASEDIR%\TestData\TestDataV8219.txt

rem SET OPTIONS=details=1

java %JAVA_VERSION% -Xms512m -Xmx512m -Djava.util.logging.config.file="%BASEDIR%\config\logging.properties" ^
		-classpath "%HH_CLASSPATH%" com.mmm.cms.homehealth.test.HomeHealthGrouper_HP ^
		"input=%TEST_FILE%" "config=%BASEDIR%\config\HomeHealthGrouper.properties" ^
		%OPTIONS%

rem reset all variables to prevent locking
SET BASEDIR=
SET HH_CLASSPATH=
SET TEST_FILE=
SET OPTIONS=

ENDLOCAL
