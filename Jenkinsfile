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
				maven 'apache-maven-3.6.1'
			}
			steps {
				powershell 'java -version'
				powershell 'mvn -version'
				powershell 'mvn package'

			}
		}
	}
}
