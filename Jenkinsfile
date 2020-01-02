 def reportPath = build.getWorkspace().child("test-output/emailable-report.html")
           msg.setContent(reportPath.readToString(), "text/html");

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
		stage('Deployment'){
			steps{
				echo "Deploying"
				deploy adapters: [tomcat7(credentialsId: '2262fca6-ee0c-4626-a239-37f0ae306f14', path: '', url: 'http://localhost:8085/')], contextPath: 'HappyTripAssignment', onFailure: false, war: '**/*.war'
			}
		}
		stage('Test Source') { 
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
        always {
           /* mail to: 'saritha.modiam@pratian.com', from: 'saritha.modiam@pratian.com',
                subject: "Build: ${env.JOB_NAME} -Success", 
                body: "Dear Team,\nThis is an automated mail to confirm that Release is successfully given for following  \"${env.JOB_NAME}\" build: ${env.BUILD_NUMBER}\n\nView the log at:\n ${env.BUILD_URL}\n\nBlue Ocean:\n${env.RUN_DISPLAY_URL}"*/
		emailext attachLog: true,subject:currentBuild.currentResult + " : " + env.JOB_NAME,body:reportPath, attachmentsPattern: '**/Test-Automaton-Report.html',  mimeType: 'text/html',  to: 'saritha.modiam@pratian.com'
	}
    }
	
}
