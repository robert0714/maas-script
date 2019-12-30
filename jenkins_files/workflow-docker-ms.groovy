import java.text.SimpleDateFormat;


def mvnDockerBuild (String branch ,String  filePath){

     mvnDockerBuild ( branch ,filePath,true);
}

def mvnDockerBuild (String branch ,String  filePath, boolean buildAll){
   stage ('git pull scm'){
                   checkout([$class: 'GitSCM', branches: [[name: branch]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: 'daa209ac-11a7-469d-9812-bed84484b135', url: 'http://172.21.11.63:9000/chain/maas-backend.git']]])
         }

   stage('mvn compile'){
            println(" build Value :  "+buildAll);
            if(buildAll){
               sh 'mvn clean install  -D env=production -P production   -Dmaven.test.skip=true '
            }else {
                println("skip build all");
            }
   }

   stage('making  docker  image...'){
              sh "mvn -f ${filePath} clean package -Dmaven.test.skip=true  -P production  docker:build docker:push "
   }
}

def deployMaasApiByDocker(String dockerServiceName,String port,String dockerImageName,String ip){
 stage('deploy'){
    node("${ip}"){
                   def dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                   def date = new Date();
                   def today = dateFormat.format(date);
                 try{
                     sh " docker rm -f ${dockerServiceName}" ;
                 }catch(any){
                     echo "failure to stop running container";
                 }
                 try{
                   sh " docker rmi  172.21.11.63:5000/robert0714/${dockerImageName}:${today}  " ;
                 }catch(any){
                     echo "failure to remove container image";
                 }
                 try{
                   sh " docker pull 172.21.11.63:5000/robert0714/${dockerImageName}:${today}  " ;
                 }catch(any){
                     echo "failure to remove container image";
                 }

                 sh    "docker run -d     --net=host   \
                    -e server.port=${port}  \
                    -e TZ=Asia/Taipei   \
                    -e eureka.client.enabled=true   \
                    -e spring.cloud.config.enabled=false  \
                    -e spring.cloud.config.discovery.enabled=false   \
                    -e service.payment.spgateway.isProduction=true  \
                    -e spring.data.mongodb.uri=mongodb://172.21.12.135:27017,172.21.12.136:27017/ec  \
                    --add-host=prod011031:172.21.11.31  \
                    --add-host=prod011032:172.21.11.32  \
                    --add-host=prod011033:172.21.11.33  \
                    --add-host=prod011034:172.21.11.34  \
                    --add-host=prod011041:172.21.11.41  \
                    --add-host=prod011042:172.21.11.42  \
                    --add-host=prod011043:172.21.11.43  \
                    --add-host=prod011046:172.21.11.46  \
                    --add-host=prod011047:172.21.11.47  \
                    --add-host=prod011048:172.21.11.48  \
                    --add-host=prod011049:172.21.11.49  \
                    --add-host=prod012135:172.21.12.135  \
                    --add-host=prod012136:172.21.12.136  \
                    --add-host=prod012151:172.21.12.151  \
                    --add-host=prod012152:172.21.12.152  \
                    --add-host=prod012131:172.21.12.131  \
                    --add-host=prod012132:172.21.12.132  \
                    --add-host=prod012133:172.21.12.133  \
                    --add-host=prod012134:172.21.12.134  \
                    -e eureka.client.serviceUrl.defaultZone=http://172.21.11.31:8761/eureka,http://172.21.11.32:8761/eureka  \
                    -e spring.boot.admin.url=http://172.21.11.41:8173/sba  \
                    -e spring.boot.admin.username=admin  \
                    -e spring.boot.admin.password=admin   \
                    -e spring.boot.admin.client.metadata.user.name=client  \
                    -e spring.boot.admin.client.metadata.user.password=client  \
                    -e  service.payment.spgateway.merchantID=UMJ20180112  \
                    -e  service.payment.spgateway.hashKey=z4tXlwa46dBNxyqUJ4hQm2rM40qrQmAE  \
                    -e  service.payment.spgateway.hashIV=JpNAAxXeTlR9NU5S  \
                    -e  service.payment.spgateway.partner.hashKey=JTjmgfleFcQ8hNvHNgxZsdIFEE0Qsfo3  \
                    -e  service.payment.spgateway.partner.hashIV=EYUEiCcXptXZgzKA   \
                    -e  service.payment.spgateway.partnerID=UMAJI10692   \
                    -e  service.payment.spgateway.isProduction=true  \
                    -e usestream=false  \
                    -e spring.cloud.stream.bindings.input.destination=hamipointTopic \
                    -e spring.cloud.stream.bindings.input.content-type=application/json \
                    -e spring.cloud.stream.kafka.binder.zkNodes=172.21.11.41 \
                    -e spring.cloud.stream.kafka.binder.brokers=172.21.11.41 \
                    -e spring.cloud.stream.kafka.binder.defaultBrokerPort=15672    \
                    -e spring.cloud.stream.kafka.binder.defaultZkPort=5672    \
                    -e redis.server=172.21.11.41 \
                    -e redis.port=5001   \
                    -e spring.zipkin.baseUrl=http://172.21.11.41:8174  \
                    -v ~/${dockerServiceName}:/root  \
                    --name ${dockerServiceName}    172.21.11.63:5000/robert0714/${dockerImageName}:${today}  ";
              }
        }
}

return this ;
