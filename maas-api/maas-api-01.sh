export  count=$( ps -aef|grep platform  -c)

echo "value:  $count "
if [[ $count > 1  ]]; then
   kill -9 $( ps -aef|grep platform  |head -1|awk '{print$2}' )  
fi

#nohup  /usr/bin/java -jar $PWD/develop/platform-napi/0.0.1-SNAPSHOT/platform-napi-0.0.1-SNAPSHOT.jar  0<&- &>   $PWD/api.log   &

nohup  java -jar /data/jenkins_slaves/vip-npp/workspace/maas-api-03//develop/platform-napi/0.0.1-SNAPSHOT/platform-napi-0.0.1-SNAPSHOT.jar  0<&- &>   $PWD/api.log   &

ps -aef|grep platform

curl http://192.168.57.75:8080/api/  |jq '.' 
