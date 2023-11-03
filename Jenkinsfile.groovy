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
                    print 'Hello World!'
                }
            }
        }
		stage('UTests') {
            steps {
                script {
                    print 'Hello World!'
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
                    print 'Hello World!'
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