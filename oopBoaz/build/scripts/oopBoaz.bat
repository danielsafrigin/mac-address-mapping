@if "%DEBUG%" == "" @echo off
@rem ##########################################################################
@rem
@rem  oopBoaz startup script for Windows
@rem
@rem ##########################################################################

@rem Set local scope for the variables with windows NT shell
if "%OS%"=="Windows_NT" setlocal

set DIRNAME=%~dp0
if "%DIRNAME%" == "" set DIRNAME=.
set APP_BASE_NAME=%~n0
set APP_HOME=%DIRNAME%..

@rem Add default JVM options here. You can also use JAVA_OPTS and OOP_BOAZ_OPTS to pass JVM options to this script.
set DEFAULT_JVM_OPTS=

@rem Find java.exe
if defined JAVA_HOME goto findJavaFromJavaHome

set JAVA_EXE=java.exe
%JAVA_EXE% -version >NUL 2>&1
if "%ERRORLEVEL%" == "0" goto init

echo.
echo ERROR: JAVA_HOME is not set and no 'java' command could be found in your PATH.
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:findJavaFromJavaHome
set JAVA_HOME=%JAVA_HOME:"=%
set JAVA_EXE=%JAVA_HOME%/bin/java.exe

if exist "%JAVA_EXE%" goto init

echo.
echo ERROR: JAVA_HOME is set to an invalid directory: %JAVA_HOME%
echo.
echo Please set the JAVA_HOME variable in your environment to match the
echo location of your Java installation.

goto fail

:init
@rem Get command-line arguments, handling Windows variants

if not "%OS%" == "Windows_NT" goto win9xME_args

:win9xME_args
@rem Slurp the command line arguments.
set CMD_LINE_ARGS=
set _SKIP=2

:win9xME_args_slurp
if "x%~1" == "x" goto execute

set CMD_LINE_ARGS=%*

:execute
@rem Setup the command line

set CLASSPATH=%APP_HOME%\lib\oopBoaz.jar;%APP_HOME%\lib\activation-1.1.jar;%APP_HOME%\lib\JavaAPIforKml-2.2.0.jar;%APP_HOME%\lib\jaxb-api-2.1.jar;%APP_HOME%\lib\jaxb-impl-2.1.17.jar;%APP_HOME%\lib\jsr173_api-1.0.jar;%APP_HOME%\lib\JavaAPIforKml-2.2.1.jar;%APP_HOME%\lib\jaxb-xjc-2.2.jar;%APP_HOME%\lib\jaxb-impl-2.2.jar;%APP_HOME%\lib\jaxb-api-2.3.0.jar;%APP_HOME%\lib\junit-jupiter-api-5.0.0.jar;%APP_HOME%\lib\commons-io-2.6.jar;%APP_HOME%\lib\mysql-connector-java-5.1.45.jar;%APP_HOME%\lib\junit-platform-commons-1.0.0.jar;%APP_HOME%\lib\apiguardian-api-1.0.0.jar;%APP_HOME%\lib\opentest4j-1.0.0.jar

@rem Execute oopBoaz
"%JAVA_EXE%" %DEFAULT_JVM_OPTS% %JAVA_OPTS% %OOP_BOAZ_OPTS%  -classpath "%CLASSPATH%" main.java.gui.Gui %CMD_LINE_ARGS%

:end
@rem End local scope for the variables with windows NT shell
if "%ERRORLEVEL%"=="0" goto mainEnd

:fail
rem Set variable OOP_BOAZ_EXIT_CONSOLE if you need the _script_ return code instead of
rem the _cmd.exe /c_ return code!
if  not "" == "%OOP_BOAZ_EXIT_CONSOLE%" exit 1
exit /b 1

:mainEnd
if "%OS%"=="Windows_NT" endlocal

:omega
