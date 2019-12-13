pipeline {
	agent any
	stages {
		stage('Source') { 
			steps {
				checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/prabhavagrawal/discoveri-happytrip.git']]])
			}
		}
		stage('Build') { 
			tools {
				jdk 'jdk7'
				maven 'apache-maven-3.5.4'
			}
			steps {
				powershell 'java -version'
				powershell 'mvn -version'
			}
		}
		stage('Deploy') {
			steps{
				echo "Deploying"
				deploy adapters: [tomcat7(credentialsId: 'cc6538f6-9343-4acc-b3fd-1309b39ce983', path: '', url: 'http://localhost:8080')], contextPath: '/happytrip', war: '**/*.war'
			}
		}
	}
}
