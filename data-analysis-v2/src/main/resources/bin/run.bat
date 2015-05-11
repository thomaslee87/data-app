echo off
set DIR=%~dp0..
set LOAD_FILE=%1%
set LIBS=%DIR%\libs
set CONF=%DIR%\conf
set MAIN_CLASS=com.intellbi.data_analysis.DataRunner

echo java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% -c %CONF% --month=201408 --exclude=201401,201402,201403
java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% -c %CONF% --month=201408 --exclude=201401,201402,201403

echo java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% -c %CONF% --month=201409
java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% -c %CONF% --month=201409

echo java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% -c %CONF% --month=201410
java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% -c %CONF% --month=201410

echo java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% -c %CONF% --month=201411
java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% -c %CONF% --month=201411

echo java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% -c %CONF% --month=201412
java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% -c %CONF% --month=201412

echo java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% -c %CONF% --month=201501
java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% -c %CONF% --month=201501

