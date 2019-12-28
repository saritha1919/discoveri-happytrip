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
               stage('Archiving Artifacts') { 
                         steps{ 
                             archiveArtifacts 'target/*.war' 
                           } 
              } 
		stage('Sonar Qube analysis') {
			steps {
				 withSonarQubeEnv('SonarQube') {
                                    powershell 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
                                    }
                           }
                }
	}
}
