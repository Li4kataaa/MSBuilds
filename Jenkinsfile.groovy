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
        stage('Restore NuGet For Solution') {
            steps {
			script {
                    bat 'nuget restore ConsoleApp1.sln'
                }
                //  '--no-cache' to avoid a shared cache--if multiple projects are running NuGet restore, they can collide.
                //bat "dotnet restore --nologo --no-cache"
            }
			}
        }
        stage('Build Solution') {
            steps {
                bat 'msbuild ConsoleApp1.sln /p:Configuration=Release'
            }
        }
		stage('Unit Testing Stage') {
            steps {
			script {
                bat 'dotnet test HelloWordTests/HelloWordTests.csproj'
            }
			}
        }
		stage('Test Enviroment Deploy') {
            steps {
			script {
                bat 'xcopy /s /y "ConsoleApp1\\*" "\\test-server\\wwwroot"'
            }
			}
        }
        stage('Manual Approval for Production Deployment') {
            steps {
                script {
                    // Pause and wait for manual approval
                    input message: 'Proceed with production deployment?', submitter: 'admin'
                }
            }
        }
		stage('Production Enviroment Deploy') {
            steps {
                script {
				bat 'xcopy /s /y "ConsoleApp1\\*" "\\production-server\\wwwroot"'
            }
			}
        }
    }
    post {
        cleanup {
            cleanWs(deleteDirs: true, disableDeferredWipeout: true, notFailBuild: true)
        }
    }
}
