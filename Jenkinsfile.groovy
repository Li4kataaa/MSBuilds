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
                // '--no-cache' to avoid a shared cache--if multiple projects are running NuGet restore, they can collide.
                bat "dotnet restore --nologo --no-cache"
            }
        }
        stage('Build Solution') {
            steps {
                bat "dotnet build --nologo -c Release -p:ProductVersion=1.0.${env.BUILD_NUMBER}.0 --no-restore"
            }
        }
        stage('Unit Test Stage') {
            steps {
                // Add your unit testing commands here.
            }
        }
        stage('Test Environment Deploy') {
            steps {
                // Add your deployment commands for the test environment here.
            }
        }
        stage('Production Environment Deploy') {
            steps {
                // Add your deployment commands for the production environment here.
            }
        }
    }
    post {
        cleanup {
            cleanWs(deleteDirs: true, disableDeferredWipeout: true, notFailBuild: true)
        }
    }
}
