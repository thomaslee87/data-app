set DIR=%~dp0..
set LOAD_FILE=%1%
set LIBS=%DIR%\libs
set CONF=%DIR%\conf
set MAIN_CLASS=com.intellbit.v2.data.tools.UserImportRunner
java -Djava.ext.dirs=%LIBS% %MAIN_CLASS% %CONF% D:\\z-proj\\2014����ļ��Ⱥ�Լ�������տͻ���ϸ.xlsx