echo off
set DIR=%~dp0..
set LOAD_FILE=%1%
set LIBS=%DIR%\libs
set CONF=%DIR%\conf
set MAIN_CLASS=com.intellbit.v2.data.tools.UserImportRunner

echo java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% %CONF% E:\data2015\2015.xlsx
java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% %CONF% E:\data2015\2015.xlsx