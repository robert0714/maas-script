export  count=$( ps -aef|grep maas   -c)

echo "value:  $count "
if [[ $count > 1  ]]; then
   kill -9 $( ps -aef|grep  maas   |head -1|awk '{print$2}' )  
fi

#nohup  /usr/bin/java -jar $PWD/develop/platform-napi/0.0.1-SNAPSHOT/platform-napi-0.0.1-SNAPSHOT.jar  0<&- &>   $PWD/api.log   &
sleep 5s
nohup  java -jar ~/maas-api.jar  0<&- &>   ~/api.log   &

ps -aef|grep maas

#curl http://192.168.57.75:8080/api/  |jq '.' 
