pipeline {
    agent any

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
                    echo 'Installing frontend dependencies...'
                    bat 'npm install'
                    echo 'Building frontend with Vite...'
                    bat 'npm run build'
                }
            }
        }

        stage('Start Backend') {
            steps {
                echo 'Starting backend service on port 8081...'
                bat 'start /B java -jar backend\\target\\backend-0.0.1-SNAPSHOT.jar --server.port=8081'
                sleep time: 5, unit: 'SECONDS'
            }
        }

        stage('Archive Artifacts') {
            steps {
                echo 'Archiving build artifacts...'
                archiveArtifacts artifacts: 'backend/target/*.jar', fingerprint: true
            }
        }



        stage('Load Test Backend') {
            steps {
                echo 'Performing backend loadtest'
                bat '"C:\\Program Files\\jmeter\\apache-jmeter-5.6.3\\bin\\jmeter.bat" -n -t tests\\loadtest.jmx -l tests\\results.jtl'
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
