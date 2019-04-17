export count_tmp=$(/sbin/lsof -i:8080 |grep java -c )
/sbin/lsof -i:8080
if [[ $count_tmp > 1 ]]; then
   /bin/curl -i -X POST http://localhost:8080/api/manage/shutdown
   /sbin/lsof -i:8080 |grep java |awk '{print$2}'
   kill -9 $(/sbin/lsof -i:8080 |grep java |awk '{print$2}' )
fi

export  count=$( ps -aef|grep maas-api   -c)

echo "value:  $count "
if [[ $count > 1  ]]; then
   kill -9 $( ps -aef|grep  maas-api   |head -1|awk '{print$2}' )
fi



sleep 5s


nohup  java -jar ~/maas-api*.jar   --image.url=https://api.umaji.com.tw/api/images/    --service.payment.spgateway.isProduction=true --service.payment.spgateway.merchantID=UMJ20180112  --service.payment.spgateway.hashKey=z4tXlwa46dBNxyqUJ4hQm2rM40qrQmAE --service.payment.spgateway.hashIV=JpNAAxXeTlR9NU5S \
  --eureka.client.enabled=true   \
                    --spring.cloud.config.enabled=false  \
                    --spring.cloud.config.discovery.enabled=false   \
                    --eureka.client.serviceUrl.defaultZone=http://172.21.11.31:8761/eureka,http://172.21.11.32:8761/eureka \
                    --spring.boot.admin.url=http://172.21.11.41:8173/sba  \
                    --spring.zipkin.baseUrl=http://172.21.11.41:8174  \
                    --spring.boot.admin.username=admin  \
                    --spring.boot.admin.password=admin   \
                    --spring.boot.admin.client.metadata.user.name=client  \
                    --spring.boot.admin.client.metadata.user.password=client  \
                    --service.hamipoint.activityno=4949  \
                    --maas.service.api.log.isOpen=true  \
    0<&- &>   ~/api.log   &

ps -aef|grep maas
