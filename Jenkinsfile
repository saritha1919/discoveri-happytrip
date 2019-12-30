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
				  timeout(time:5, unit:'MINUTES') {
                                        input message:'Approve deployment?'
                                   }
                         } 
                 } 
		stage('Sonar Qube analysis') {
			steps {
				 withSonarQubeEnv('SonarQube') {
                                    powershell 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar'
                                    }
                           }
                }
		
		stage('Deployment'){
			steps{
				echo "Deploying"
				deploy adapters: [tomcat7(credentialsId: '2262fca6-ee0c-4626-a239-37f0ae306f14', path: '', url: 'http://localhost:8085/')], contextPath: 'HappyTripAssignment', onFailure: false, war: '**/*.war'
			}
		}
		stage('Source') { 
			steps {
				checkout([$class: 'GitSCM', branches: [[name: '*/master']], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[url: 'https://github.com/saritha1919/HappyTripAssignment-TestCases.git']]])
			 }
		 }
		  stage('Testing') {  
                    tools { 
                           jdk 'jdk8' 
                           maven 'Maven' 
                    } 
                   steps { 
                      powershell 'java -version' 
                      powershell 'mvn -version' 
                     // bat label: '', script: 'testNGBatchfile.bat' 
			   powershell 'mvn test'
                    } 
                 } 
    }
	post {
        success {
            mail to: 'saritha.modiam@pratian.com, prabhav@pratian.com', from: 'saritha.modiam@pratian.com',
                subject: "Build: ${env.JOB_NAME} -Success", 
                body: "Dear Team,\nThis is an automated mail to confirm that Release is successfully given for following  \"${env.JOB_NAME}\" build: ${env.BUILD_NUMBER}\n\nView the log at:\n ${env.BUILD_URL}\n\nBlue Ocean:\n${env.RUN_DISPLAY_URL}"
        }
    }
	
}
