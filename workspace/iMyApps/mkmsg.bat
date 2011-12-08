call c:\java\j6.bat

native2ascii -encoding UTF-8 messages\Messages.properties src\net\imyapps\gwt\server\Messages.properties
native2ascii -encoding UTF-8 messages\Messages_zh_TW.properties src\net\imyapps\gwt\server\Messages_zh_TW.properties
native2ascii -encoding UTF-8 messages\Messages_zh_CN.properties src\net\imyapps\gwt\server\Messages_zh_CN.properties

copy src\net\imyapps\gwt\server\Messages.properties bin\net\imyapps\gwt\server\Messages.properties
copy src\net\imyapps\gwt\server\Messages_zh_TW.properties bin\net\imyapps\gwt\server\Messages_zh_TW.properties
copy src\net\imyapps\gwt\server\Messages_zh_CN.properties bin\net\imyapps\gwt\server\Messages_zh_CN.properties
