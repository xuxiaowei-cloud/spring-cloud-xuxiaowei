env.XUXIAOWEI_DATABASE_HOST = 'mysql'
env.XUXIAOWEI_DATABASE_USERNAME = 'root'
env.XUXIAOWEI_DATABASE_PASSWORD = 'xuxiaowei.com.cn'
env.XUXIAOWEI_DATABASE = 'spring_cloud_xuxiaowei'

env.XUXIAOWEI_NACOS_MYSQL_HOST = "${env.XUXIAOWEI_DATABASE_HOST}"
env.XUXIAOWEI_NACOS_MYSQL_USERNAME = "${env.XUXIAOWEI_DATABASE_USERNAME}"
env.XUXIAOWEI_NACOS_MYSQL_PASSWORD = "${env.XUXIAOWEI_DATABASE_PASSWORD}"
env.XUXIAOWEI_NACOS_MYSQL_DATABASE = 'spring_cloud_xuxiaowei_nacos'

env.XUXIAOWEI_REDIS_HOST = 'redis'
env.XUXIAOWEI_REDIS_PASSWORD = ''

env.MAVEN_OPTS = '-Dmaven.repo.local=/root/.m2/repository'

node {
    checkout scm

    docker.image('mysql:8.0.36').withRun('-e "MYSQL_ROOT_PASSWORD=${XUXIAOWEI_DATABASE_PASSWORD}" -e "MYSQL_DATABASE=${XUXIAOWEI_DATABASE}"') { c1 ->

        docker.image('mysql:8.0.36').inside("--link ${c1.id}:${XUXIAOWEI_DATABASE_HOST}") {
            /* Wait until mysql service is up */
            sh 'while ! mysqladmin ping -h$XUXIAOWEI_DATABASE_HOST --silent; do sleep 1; done'

            sh 'mysql -u ${XUXIAOWEI_DATABASE_USERNAME} -p${XUXIAOWEI_DATABASE_PASSWORD} --host=${XUXIAOWEI_DATABASE_HOST} ${XUXIAOWEI_DATABASE} < sql/1-nacos-mysql-schema.sql'
            sh 'mysql -u ${XUXIAOWEI_DATABASE_USERNAME} -p${XUXIAOWEI_DATABASE_PASSWORD} --host=${XUXIAOWEI_DATABASE_HOST} ${XUXIAOWEI_DATABASE} < sql/2-nacos-mysql-data.sql'
            sh 'mysql -u ${XUXIAOWEI_DATABASE_USERNAME} -p${XUXIAOWEI_DATABASE_PASSWORD} --host=${XUXIAOWEI_DATABASE_HOST} ${XUXIAOWEI_DATABASE} < sql/3-security-mysql-schema.sql'
            sh 'mysql -u ${XUXIAOWEI_DATABASE_USERNAME} -p${XUXIAOWEI_DATABASE_PASSWORD} --host=${XUXIAOWEI_DATABASE_HOST} ${XUXIAOWEI_DATABASE} < sql/4-security-mysql-data.sql'
            sh 'mysql -u ${XUXIAOWEI_DATABASE_USERNAME} -p${XUXIAOWEI_DATABASE_PASSWORD} --host=${XUXIAOWEI_DATABASE_HOST} ${XUXIAOWEI_DATABASE} < sql/5-oauth-2.1-mysql-schema.sql'
            sh 'mysql -u ${XUXIAOWEI_DATABASE_USERNAME} -p${XUXIAOWEI_DATABASE_PASSWORD} --host=${XUXIAOWEI_DATABASE_HOST} ${XUXIAOWEI_DATABASE} < sql/6-oauth-2.1-mysql-data.sql'
        }

        docker.image('redis:7.2.4').withRun() { c2 ->

            docker.image('maven:3.6.3-openjdk-17').inside("--link ${c1.id}:${XUXIAOWEI_DATABASE_HOST} --link ${c2.id}:${XUXIAOWEI_REDIS_HOST} --user 0 -v /root/.m2:/root/.m2") {

                sh 'id'

                sh 'mvn clean package source:jar javadoc:jar -U -DskipTests=true -s settings.xml'

                sh 'mv nacos/target/nacos-0.0.1-SNAPSHOT.jar /root/nacos-0.0.1-SNAPSHOT.jar'
                sh 'nohup java -Xms128m -Xmx256m -jar /root/nacos-0.0.1-SNAPSHOT.jar > nohup-nacos.out 2>&1 &'
                sh 'sleep 20s'
                sh 'cat nohup-nacos.out'
                sh 'curl "http://127.0.0.1:8848/nacos/v1/cs/configs?dataId=&group=&appName=&config_tags=&pageNo=1&pageSize=10&tenant=&search=blur" && echo'
                sh 'cat nohup-nacos.out'

                sh 'nohup java -Xms128m -Xmx256m -jar spring-cloud-xuxiaowei-passport/target/spring-cloud-xuxiaowei-passport-0.0.1-SNAPSHOT.jar > nohup-passport.out 2>&1 &'
                sh 'sleep 20s'
                sh 'cat nohup-passport.out'

                sh 'nohup java -Xms128m -Xmx256m -jar spring-cloud-xuxiaowei-file/target/spring-cloud-xuxiaowei-file-0.0.1-SNAPSHOT.jar > nohup-file.out 2>&1 &'
                sh 'sleep 20s'
                sh 'cat nohup-file.out'

                sh 'mvn test -DskipTests=false -s settings.xml'
            }
        }
    }
}