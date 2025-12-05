pipeline {
    agent any

    environment {
        NODE_ENV = 'production'
    }

    tools {
        maven  'Maven-3.9'
        nodejs 'NodeJSManual'
    }

    stages {

        stage('Build Backend') {
            steps {
                dir('backend') {
                    echo 'Building backend with Maven...'
                    sh 'mvn clean package'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    echo 'Installing frontend dependencies...'
                    sh 'npm install'
                    echo 'Building frontend with Vite...'
                    sh 'npm run build'
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                echo 'Archiving build artifacts...'
                archiveArtifacts artifacts: 'backend/target/*.jar', fingerprint: true
            }
        }

        // Optional: add deploy stage(s) below
        // stage('Deploy') {
        //     steps {
        //         echo 'Deploying application...'
        //         // Example: sh 'scp backend/target/DISC-Project.jar user@server:/deployments/'
        //     }
        // }

    }

    post {
        success {
            echo '✅ Build completed successfully.'
        }
        failure {
            echo '❌ Build failed. Check logs for details.'
        }
    }
}
