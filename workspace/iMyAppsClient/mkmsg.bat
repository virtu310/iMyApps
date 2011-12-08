call c:\java\j6.bat

native2ascii -encoding UTF-8 messages\IpaChecker.properties src\net\imyapps\client\IpaChecker.properties
native2ascii -encoding UTF-8 messages\IpaChecker_zh_TW.properties src\net\imyapps\client\IpaChecker_zh_TW.properties
native2ascii -encoding UTF-8 messages\IpaChecker_zh_CN.properties src\net\imyapps\client\IpaChecker_zh_CN.properties

copy src\net\imyapps\client\IpaChecker.properties bin\net\imyapps\client\IpaChecker.properties
copy src\net\imyapps\client\IpaChecker_zh_TW.properties bin\net\imyapps\client\IpaChecker_zh_TW.properties
copy src\net\imyapps\client\IpaChecker_zh_CN.properties bin\net\imyapps\client\IpaChecker_zh_CN.properties