@echo off
title MasjidKU TV Localhost Server (Port 8080)
echo ========================================================
echo   MasjidKU TV — Smart Digital Signage & Remote Server
echo ========================================================
echo.
echo Menjalankan local server di port 8080...
echo.

set PYTHON_PATH="C:\Users\T6N0CV02S35823C\AppData\Local\Programs\Python\Python312\python.exe"

if exist %PYTHON_PATH% (
    %PYTHON_PATH% server.py
) else (
    python server.py
)

pause
