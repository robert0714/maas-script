sh " kill -9 $( ps -aef|grep platform  |head -1|awk '{print$2}' )  "

sh " nohup  /usr/bin/java -jar $PWD/develop/platform-napi/0.0.1-SNAPSHOT/platform-napi-0.0.1-SNAPSHOT.jar 0<&- &>  $PWD/api.log & "

curl http://192.168.57.75:8080/api/  |jq '.' 
