pipeline {
	
	agent any
	stages {
		stage('Source') { 
			steps {
				checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/saritha1919/discoveri-happytrip.git']]])
			}
		}
		
		stage('Sonarqube analysis') {
                 steps {
                      script {
                           scannerHome = tool 'SonarScannerGlobalTool';
                        }
                          withSonarQubeEnv('SonarConfigure') {
                            bat "${scannerHome}/bin/sonar-scanner.bat" 
                         }
                    }
                }
	}
}
