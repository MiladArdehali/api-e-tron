@echo off
mode con: cols=60 lines=50
echo Exectution SpringBoot ETRON

call mvn clean
call mvn install

java -jar C:\Users\sii\Desktop\robot\e-tron\target\pi4led-1.0-SNAPSHOT.jar
pause