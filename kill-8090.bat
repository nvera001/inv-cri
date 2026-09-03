@echo off
setlocal enabledelayedexpansion
set FOUND=0

for /f "tokens=5" %%p in ('netstat -aon ^| findstr :8090 ^| findstr LISTENING') do (
    echo Matando proceso con PID %%p en el puerto 8090...
    taskkill /PID %%p /F
    set FOUND=1
)

if !FOUND! == 0 (
    echo No habia nada escuchando en el puerto 8090.
)