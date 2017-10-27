node {
   def mvnHome
   stage('checkout') { // for display purposes
      git 'https://github.com/mmuniz75/askalien-wildfly.git'
      mvnHome = tool 'maven'
   }
   stage('package') {
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
   
    env.DOCKER_HOME = "${tool 'docker'}"
    env.PATH="${env.DOCKER_HOME}/bin:${env.PATH}"
    
    stage('Build image') {
     sh "docker rmi mmuniz/askalien:mythi-wildfly -f || true"   
     sh 'docker build -t mmuniz/askalien:mythi-wildfly .'
    } 
    
    stage('Push image') {
        sh 'docker push mmuniz/askalien:mythi-wildfly'
    }   

    stage('Deploy on AWS') {
        env.PATH="/var/jenkins_home/.local/bin:${env.PATH}"
	    sh "cd config/aws && eb deploy"
    }    
    
}
