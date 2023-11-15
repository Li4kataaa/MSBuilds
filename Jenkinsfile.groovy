pipeline {
    // Run on any available Jenkins agent.
    agent any
	environment {
        MSDeployPath = 'C:\\Program Files\\IIS\\Microsoft Web Deploy V3\\msdeploy.exe'
        WebDeployUsername = 'your_web_deploy_username'
        WebDeployPassword = credentials('your_web_deploy_password_id')
        WebServerAddress = 'your_web_server_address'
        SiteName = 'your_website_name'
    }
    options {
        // Show timestamps in the build.
        timestamps()
        // Prevent more than one build from running at a time for this project.
        disableConcurrentBuilds()
        // If Jenkins restarts or the client disconnects/reconnects, abandon the current build instead of trying to continue.
        disableResume()
    }
    triggers {
        // Poll source control periodically for changes.
        pollSCM 'H * * * *'
    }
    stages {
        stage('Build!') {
            steps {
                script {
			bat "dotnet restore --nologo --no-cache"
			def projectDirectory = "C:\\data\\jenkins_home\\workspace\\ldtestProjects_MSBuilds_MSBuilds\\ConsoleApp\\ConsoleApp1"
                    dir(projectDirectory) {
                        bat "dotnet build -c Release"
						bat "dotnet publish -c Release -o D:\\PublishedWebSites\\ConsoleApp"
		    }
			print 'Excelent Build!'
                }
            }
        }
		stage('UTests') {
            steps {
                script {
			def projectDirectory = "C:\\data\\jenkins_home\\workspace\\ldtestProjects_MSBuilds_MSBuilds\\ConsoleApp\\HelloWorldTests"
                    dir(projectDirectory) {
			bat "dotnet test"
		    }
                        print 'Excelent Tests!'
                }
            }
        }
		stage('Vulnerability Check') {
            steps {
                script {
                    print 'NOT IMPLEMENTED!'
                }
            }
        }
		stage('Deploy Test') {
            steps {
                script {
                    // MSDeploy deployment
                    bat "${MSDeployPath} -verb:sync -source:package=target\\your_web_app_package.zip -dest:auto,computerName=${WebServerAddress},userName=${WebDeployUsername},password=${WebDeployPassword},authType=basic -setParam:name='IIS Web Application Name',value=${SiteName}"
                   print 'NOT IMPLEMENTED!'
                }
            }
        }
		stage('Manual Approval for Production Deployment') {
            steps {
                script {
                   def userInput = input(
                        message: 'Approve Production Deployment',
                        ok: 'Deploy',
                        submitter: 'ilian_sharkov@dware.bg'
                    )
                    //if (userInput == null || userInput == false) {
                    //   error('Production deployment not approved. Aborting.')
                    //}
                }
				nexusArtifactUploader(
        nexusVersion: 'nexus3',
        protocol: 'http',
        nexusUrl: 'my.nexus.address',
        groupId: 'com.example',
        version: 1.0.0.0.1,
        repository: "C:\\data\\jenkins_home\\workspace\\ldtestProjects_MSBuilds_MSBuilds\\ConsoleApp\\ConsoleApp1",
        credentialsId: 'CredentialsId',
        artifacts: [
            [artifactId: WebApplication1,
             classifier: '',
             file: 'my-service-' + version + '.zip',
             type: 'zip']
        ]
            }
        }
		stage('Deploy Production') {
            steps {
                script {
                    print 'NOT IMPLEMENTED!'
                }
            }
        }
    }

//post {
        //cleanup {
          //  cleanWs(deleteDirs: true, disableDeferredWipeout: true, notFailBuild: true)
        //}
    //}
}
