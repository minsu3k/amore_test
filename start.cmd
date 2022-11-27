@echo off
call gradlew build
call java -jar .\build\libs\test-0.0.1-SNAPSHOT.jar
pause