pipeline {
    agent any

    tools {
        nodejs 'NodeJS_18'     // Or whatever version label you configured in Jenkins
        maven 'Maven_3.8.7'    // Replace with your actual Maven label in Jenkins
    }

    stages {
        stage('Build Backend') {
            steps {
                dir('backend') {
                    sh 'mvn clean package'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    sh 'npm install'
                    sh 'npm run build'
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                archiveArtifacts artifacts: '**/target/*.jar', fingerprint: true
            }
        }
    }
}
