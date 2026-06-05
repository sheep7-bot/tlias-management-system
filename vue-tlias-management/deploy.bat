@echo off
chcp 65001 >nul
echo ========================================
echo  Tlias 前端构建 + 部署到 Nginx
echo ========================================
echo.

:: 1. 构建前端
echo [1/3] 正在构建前端...
call npm run build
if %errorlevel% neq 0 (
    echo [ERROR] 构建失败，请检查代码错误
    pause
    exit /b 1
)
echo [OK] 构建完成
echo.

:: 2. 复制到 Nginx
echo [2/3] 正在部署到 Nginx...
xcopy /E /Y /Q dist\* "E:\Develop\nginx-1.22.0-web\html\"
if %errorlevel% neq 0 (
    echo [ERROR] 复制失败
    pause
    exit /b 1
)
echo [OK] 部署完成
echo.

:: 3. 重启 Nginx（让新文件生效）
set NGINX_DIR=E:\Develop\nginx-1.22.0-web
echo [3/3] 重启 Nginx...
%NGINX_DIR%\nginx -s reload 2>nul
if %errorlevel% neq 0 (
    %NGINX_DIR%\nginx -s quit 2>nul
    timeout /t 1 /nobreak >nul
    start "" "%NGINX_DIR%\nginx.exe"
)
echo [OK] Nginx 已刷新
echo.

echo ========================================
echo  部署成功！
echo  访问 http://localhost:90 查看效果
echo ========================================
pause
