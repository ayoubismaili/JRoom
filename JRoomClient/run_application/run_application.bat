rem Script de l'éxecution du client

rem JavaFX est incorporée au niveau du dossier javafx-sdk-11.0.2
set JAVAFX_SDK_11=%CD%\javafx-sdk-11.0.2\lib

rem JDK 13 doit être installée
set JAVA_JDK_13="C:\Program Files\Java\jdk-13.0.2"
%JAVA_JDK_13%\bin\java.exe --module-path %JAVAFX_SDK_11% --add-modules=javafx.controls,javafx.fxml -jar ..\dist\JRoomClient.jar
pause
