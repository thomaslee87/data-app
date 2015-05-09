set DIR=%~dp0..
set LOAD_FILE=%1%
set LIBS=%DIR%\libs
set CONF=%DIR%\conf
set MAIN_CLASS=com.intellbi.data_analysis.DataRunner

echo java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% -c %CONF% --month=201406 --exclude=201401,201402,201403
java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% -c %CONF% --month=201406 --exclude=201401,201402,201403