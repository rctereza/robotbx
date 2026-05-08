############################################################################################################
#AT THE ECLIPSE TERMINAL YOU CAN EXECUTE THIS COMMANDS BELOW TO RUN THIS APP

#RUN WITHOUT USING A JAR FILE
java --module-path bin;libs;libs\ocr; -m com.rctereza.robotbx/com.rctereza.robotbx.Main

#RUN USING A JAR FILE, FIRST CREATE THE JAR FILE
jar --create --file exp/robotbx.jar --main-class com.rctereza.robotbx.Main --module-version 1.0 -C bin\ .

#THEN RUN THE JAR FILE
java --module-path exp/robotbx.jar;libs;libs\ocr --module com.rctereza.robotbx

--------------------------------------------------------------------------------------------------------------------------
#THIS COMMAND SHOWS THE MODULE CONTENT
jar -f exp/robotbx.jar -d

#THIS COMMAND SHOWS THE JAR CONTENT
jar -f exp/robotbx.jar --list

#THESE COMMANDS SHOW the package-level or class-level dependencies of Java class files
jdeps --module-path exp;libs;libs\ocr -m com.rctereza.robotbx --multi-release 9
jdeps --module-path exp;libs;libs\ocr -m com.rctereza.robotbx --multi-release 9 -s

--------------------------------------------------------------------------------------------------------------------------
#The jlink tool links a set of modules, along with their transitive dependences, to create a custom runtime image.
rmdir /s /q customjre21
jlink -p "%JAVA_HOME%\jmods" --output customjre21 --strip-debug --no-man-pages --no-header-files --compress=zip-9 --add-modules java.base,java.datatransfer,java.desktop,java.prefs,java.logging

--------------------------------------------------------------------------------------------------------------------------
#The jpackage tool will create the msi file to install your application to the client's computer.
#To help you see any problem during the iniatlization of your app use the parameter --win-console otherwise run it without this parameter.
jpackage --module-path "%JAVA_HOME%\jmods";exp;libs;libs\ocr --module com.rctereza.robotbx --runtime-image customjre21 --name Robotbx --app-version 1.0 --description "Robotbx App" --vendor "PICorporation" --copyright "Copyright 2026, All rights reserved" --type msi --win-shortcut --win-menu --win-menu-group "PICorporation" --win-console
jpackage --module-path "%JAVA_HOME%\jmods";exp;libs;libs\ocr --module com.rctereza.robotbx --runtime-image customjre21 --name Robotbx --app-version 1.0 --description "Robotbx App" --vendor "PICorporation" --copyright "Copyright 2026, All rights reserved" --type msi --win-shortcut --win-menu --win-menu-group "PICorporation"

#USE THIS COMMAND TO UNINSTALL YOUR APP 
msiexec /x robotbx-1.0.msi

--------------------------------------------------------------------------------------------------------------------------
#TO ACCESS THE LOG FILE CHECK THE PATH BELOW. IT CAN BE DIFFERENT DEPENDING ON THE COMPUTER YOU ARE
# %LOCALAPPDATA%\Robotbx\logs -> C:\Users\terezarc\AppData\Local\Robotbx\logs\app.log

--------------------------------------------------------------------------------------------------------------------------
#How to check which JDK version compiled the class
#Java 11 (major version 55)
#Java 17 (major version 61)
#Java 21 (major version 65)

javap -verbose bin/com.rctereza.robotbx.Main
javap -verbose bin/com.rctereza.robotbx.Main | findstr major
