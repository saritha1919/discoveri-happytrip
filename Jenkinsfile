pipeline {
  stages {
    stage('Source') { 
      steps {
        checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: '9d351bb7-529f-4c21-99c3-efd5d84a9307', url: 'https://github.com/spgupta1/jenkinsIntegration.git']]])
      }
    }
    stage('Build') { 
      tools {
        jdk 'jdk11'
        maven 'maven-3.6.1'
      }
      steps {
        powershell 'java -version'
        powershell 'mvn -version'
      }
    }
    stage('Deploy') {
    }
  }
}
