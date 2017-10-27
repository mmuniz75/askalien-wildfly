node {
   def mvnHome
   stage('checkout') { // for display purposes
      git 'https://github.com/mmuniz75/askalien-wildfly.git'
      mvnHome = tool 'maven'
   }
   stage('build') {
     sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore clean package"
   }
   stage('config') {
     sh "sed -i -e 's|<USER_GUESS>|${env.USER_GUESS}|g' config/contrib/wfcfg/application-users.properties"
     sh "sed -i -e 's|<USER_GUESS_PASSWORD>|${env.USER_GUESS_PASSWORD}|g' config/contrib/wfcfg/application-users.properties"
     sh "sed -i -e 's|<USER_ADMIN>|${env.USER_ADMIN}|g' config/contrib/wfcfg/application-users.properties"
     sh "sed -i -e 's|<USER_ADMIN_PASSWORD>|${env.USER_ADMIN_PASSWORD}|g' config/contrib/wfcfg/application-users.properties"
     sh "sed -i -e 's|<POSTGRESQL_SERVICE_HOST>|${env.POSTGRESQL_SERVICE_HOST}|g' config/aws/.ebextensions/environmentvariables.config"
     sh "sed -i -e 's|<POSTGRESQL_PASSWORD>|${env.POSTGRESQL_PASSWORD}|g' config/aws/.ebextensions/environmentvariables.config"
     sh "sed -i -e 's|<POSTGRESQL_USER>|${env.POSTGRESQL_USER}|g' config/aws/.ebextensions/environmentvariables.config"
   }
   
    stage('Set AWS archive') {
     sh "cp Dockerfile config/aws/"  
     sh "mkdir -p config/aws/target && cp target/*.war config/aws/target/"   
     sh 'mkdir -p config/aws/config && cp -r config/contrib config/aws/config/'
    } 
    
    stage('Deploy on AWS') {
        env.PATH="/var/jenkins_home/.local/bin:${env.PATH}"
	    sh "cd config/aws && eb deploy"
    }    
    
}
