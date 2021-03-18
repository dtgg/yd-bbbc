#!/bin/bash
nohup java -jar -Xmx8096m -Xms8096m -Xmn1536m -Xss1m -XX:PermSize=1024m -XX:MaxPermSize=1024m -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=85 -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSParallelRemarkEnabled -XX:+CMSClassUnloadingEnabled /usr/local/games/bs4lottery-1.0.jar >/usr/local/games/bs4lottery.log 2>&1 &

nohup java -jar -Xmx5096m -Xms5096m -Xmn1536m -Xss1m -XX:PermSize=1024m -XX:MaxPermSize=1024m -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=85 -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSParallelRemarkEnabled -XX:+CMSClassUnloadingEnabled /usr/local/games/lobby-1.0.jar >/usr/local/games/lobby.log 2>&1 &

nohup java -jar -Xmx4096m -Xms4096m -Xmn1536m -Xss1m -XX:PermSize=1024m -XX:MaxPermSize=1024m -XX:+UseConcMarkSweepGC -XX:CMSInitiatingOccupancyFraction=85 -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=0 -XX:+CMSParallelRemarkEnabled -XX:+CMSClassUnloadingEnabled /usr/local/games/connection-1.0.jar >/usr/local/games/connection.log 2>&1 &

mvn clean compile assembly:single