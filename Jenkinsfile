pipeline {
	agent any
	stages {
		stage('Source') { 
			steps {
				checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/saritha1919/discoveri-happytrip.git']]])
			}
		}
		stage('Build') { 
			tools {
				jdk 'jdk8'
				maven 'Maven'
			}
			steps {
				powershell 'java -version'
				powershell 'mvn -version'
				powershell 'mvn clean package'
			}
		}
		stage('Archiving Artifacts')
		{
			steps{
				archiveArtifacts 'target/*.war'
			}
		}
		stage('Sonarqube analysis') {
                     steps {
                                def scannerHome = tool 'SonarScannerGlobalTool'; 
                              withSonarQubeEnv('SonarConfigure') {
                                powershell "${scannerHome}/bin/sonar-scanner -D sonar.projectKey=Happytripjavaproject -D sonar.projectName=Happytripjavaproject -D sonar.projectVersion=1.0 -D sonar.sources=src/main -D sonar.language=java" 
                  }
    }
        }
	}
}
