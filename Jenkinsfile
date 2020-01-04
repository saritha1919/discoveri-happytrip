
pipeline {
	
	agent any
	parameters{
		booleanParam(defaultValue: false, description: '', name: 'CodeAnalysis')
	        booleanParam(defaultValue: false, description: '', name: 'Deployment')
	}
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
		      echo "Selected value: "+params.CodeAnalysis
			   echo "unselected value:"+params.Deployment
			   
                    } 
                 } 
		
		stage('Sonar Qube analysis') {
			steps{
				script{
				if(params.CodeAnalysis == true){
				         withSonarQubeEnv('SonarQube') {
                                         /*powershell 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.2:sonar' +  '-f all/pom.xml ' */
					 powershell 'mvn org.sonarsource.scanner.maven:sonar-maven-plugin:3.3.0.603:sonar ' + 
                                         '-f pom.xml ' + 
				         '-Dsonar.projectKey=Happytripjavaproject' +
 					 '-Dsonar.projectName=Happytripjavaproject' +
				          '-Dsonar.projectVersion=1.0'
					 }
                                   }
                                }
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
				script{
				if(params.Deployment == true){
					
				             echo "Deploying"
				              deploy adapters: [tomcat7(credentialsId: '2262fca6-ee0c-4626-a239-37f0ae306f14', path: '', url: 'http://localhost:8085/')], contextPath: 'HappyTripAssignment', onFailure: false, war: '**/*.war'
			         }
				
		         }
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
		emailext attachLog: true,subject:currentBuild.currentResult + " : " + env.JOB_NAME,body:'${FILE,path="target/surefire-reports/emailable-report.html"}', attachmentsPattern: '**/Test-Automaton-Report.html',  mimeType: 'text/html',  to: 'saritha.modiam@pratian.com'
	}
    }	
}
