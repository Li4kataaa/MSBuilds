pipeline {
    // Run on any available Jenkins agent.
    agent any
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
                    print 'Hello World!'
                }
            }
        }
		stage('Deploy Test') {
            steps {
                script {
                    print 'Hello World!'
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
            }
        }
		stage('Deploy Production') {
            steps {
                script {
                    print 'Hello World!'
                }
            }
        }
    }
}
