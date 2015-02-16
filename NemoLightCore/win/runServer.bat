@echo off
cls
echo null>server.lock
REM start /b java -Djavax.net.debug=all -Xdebug -Xrunjdwp:transport=dt_socket,server=n,address=7777 -jar nemolight-core-0.1-jar-with-dependencies.jar %*
start /b java -jar nemolight-core-0.1-jar-with-dependencies.jar %*
wmic PROCESS where "Name='java.exe'" get ProcessID | findstr [0-9]>server.lock

@echo on