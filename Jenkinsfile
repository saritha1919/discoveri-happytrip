pipeline {
	  environment { 

      scannerHome = tool 'SonarQubeScanner'; 

   } 
	agent any
	stages {
		stage('Source') { 
			steps {
				checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/saritha1919/discoveri-happytrip.git']]])
			}
		}
		
		stage('Sonar Qube analysis') {
			steps {
                              withSonarQubeEnv('SonarQube') {
                               sh "${scannerHome}/bin/sonar-scanner"
                           }
                   }
              }
	}
}
