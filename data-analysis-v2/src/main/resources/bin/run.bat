set DIR=%~dp0..
set LOAD_FILE=%1%
set LIBS=%DIR%\libs
set CONF=%DIR%\conf
set MAIN_CLASS=com.intellbi.data_analysis.DataRunner

echo java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% -c %CONF% --month=201406 --exclude=201401,201402,201403
java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% -c %CONF% --month=201406 --exclude=201401,201402,201403



::echo java -cp lib\%runJar% com.intellbi.data_analysis.DataRunner --config=%location%\conf\config.properties --month=201406 --exclude=201401,201402,201403
::java -cp lib\%runJar% com.intellbi.data_analysis.DataRunner --config=%location%\conf\config.properties --month=201406 --exclude=201401,201402,201403

::echo java -cp lib\%runJar% com.intellbi.data_analysis.DataRunner --config=%location%\conf\config.properties --month=201407 --exclude=201401,201402,201403
::java -cp lib\%runJar% com.intellbi.data_analysis.DataRunner --config=%location%\conf\config.properties --month=201407 --exclude=201401,201402,201403

::echo java -cp lib\%runJar% com.intellbi.data_analysis.DataRunner --config=%location%\conf\config.properties --month=201408 --exclude=201401,201402,201403
::java -cp lib\%runJar% com.intellbi.data_analysis.DataRunner --config=%location%\conf\config.properties --month=201408 --exclude=201401,201402,201403

::echo java -cp lib\%runJar% com.intellbi.data_analysis.DataRunner --config=%location%\conf\config.properties --month=201409
::java -cp lib\%runJar% com.intellbi.data_analysis.DataRunner --config=%location%\conf\config.properties --month=201409

::echo java -cp lib\%runJar% com.intellbi.data_analysis.DataRunner --config=%location%\conf\config.properties --month=201410
::java -cp lib\%runJar% com.intellbi.data_analysis.DataRunner --config=%location%\conf\config.properties --month=201410

::echo java -cp lib\%runJar% com.intellbi.data_analysis.DataRunner --config=%location%\conf\config.properties --month=201411
::java -cp lib\%runJar% com.intellbi.data_analysis.DataRunner --config=%location%\conf\config.properties --month=20141
