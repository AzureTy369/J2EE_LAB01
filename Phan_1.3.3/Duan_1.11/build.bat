@echo off
setlocal
set ROOT=%~dp0
set SRC=%ROOT%src\main\java
set WEB=%ROOT%src\main\webapp
set CLASSES=%ROOT%build\classes
set TARGET=%ROOT%build\web

if not exist "%ROOT%libs" mkdir "%ROOT%libs"
if not exist "%CLASSES%" mkdir "%CLASSES%"
if not exist "%TARGET%" mkdir "%TARGET%"

if not exist "%ROOT%libs\javax.servlet-api-4.0.1.jar" (
    powershell -NoProfile -Command "Invoke-WebRequest -Uri 'https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/4.0.1/javax.servlet-api-4.0.1.jar' -OutFile '%ROOT%libs\javax.servlet-api-4.0.1.jar'"
)

javac -cp "%ROOT%libs\javax.servlet-api-4.0.1.jar" -d "%CLASSES%" ^
    "%SRC%\com\example\model\Product.java" ^
    "%SRC%\com\example\service\ProductService.java" ^
    "%SRC%\com\example\controller\ProductServlet.java"

xcopy "%WEB%\*" "%TARGET%\" /E /Y >nul
if not exist "%TARGET%\WEB-INF\classes" mkdir "%TARGET%\WEB-INF\classes"
xcopy "%CLASSES%\*" "%TARGET%\WEB-INF\classes\" /E /Y >nul

echo Build completed successfully.
