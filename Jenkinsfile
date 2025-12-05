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

        stage('Checkout SCM') {
            steps {
                checkout scm
            }
        }

        stage('Build Backend') {
            steps {
                dir('backend') {
                    echo 'Building backend with Maven...'
                    bat 'mvn clean package -DskipTests'
                }
            }
        }

        stage('Build Frontend') {
            steps {
                dir('frontend') {
                    echo 'Cleaning up build path'
                    bat 'rmdir /S /Q node_modules\\.vite-temp 2>NUL || echo temp folder not found'
                    echo 'Installing frontend dependencies...'
                    bat 'npm install'
                    echo 'Building frontend with Vite...'
                    bat 'npm run build'
                }
            }
        }

        stage('Archive Artifacts') {
            steps {
                echo 'Archiving build artifacts...'
                archiveArtifacts artifacts: 'backend/target/*.jar', fingerprint: true
            }
        }
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
