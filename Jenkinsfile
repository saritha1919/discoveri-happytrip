pipeline {
	environment {
      scannerHome = tool 'SonarScannerGlobalTool';
   }
	agent any
	stages {
		stage('Source') { 
			steps {
				checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/saritha1919/discoveri-happytrip.git']]])
			}
		}
		stage('Sonarqube analysis') {
			steps{
                              withSonarQubeEnv('SonarConfigure') {
                                powershell "${scannerHome}/bin/sonar-scanner -D sonar.projectKey=Happytripjavaproject -D sonar.projectName=Happytripjavaproject -D sonar.projectVersion=1.0 -D sonar.sources=src/main -D sonar.language=java" 
			      }
			}
        }
	}
}
