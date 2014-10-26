@echo off
cls
echo null>server.lock
start /b java -jar HomeApp-0.1-jar-with-dependencies.jar %*
wmic PROCESS where "Name='java.exe'" get ProcessID | findstr [0-9]>server.lock

@echo on