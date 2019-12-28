pipeline {
	
	agent any
	stages {
		stage('Source') { 
			steps {
				checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/saritha1919/discoveri-happytrip.git']]])
			}
		}
		stage('Sonarqube analysis') {
			environment {
      scannerHome = tool 'SonarScannerGlobalTool';
   }
			steps{
                              withSonarQubeEnv('SonarConfigure') {
                                powershell "${scannerHome}/bin/sonar-scanner"
			      }
			}
        }
	}
}
