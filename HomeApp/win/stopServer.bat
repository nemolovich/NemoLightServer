@echo off
cls
set /p pid=<server.lock
taskkill /PID %pid% /T /F
echo null>server.lock

@echo on