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

        stage('Archive Artifacts') {
            steps {
                echo 'Archiving build artifacts...'
                archiveArtifacts artifacts: 'backend/target/*.jar', fingerprint: true
            }
        }

        stage('Validate JMeter Test Plan') {
            steps {
                echo 'Validating JMeter test plan structure...'

                bat '''
                echo ===== Showing tests folder =====
                dir tests

                echo ===== Display file contents =====
                type tests\\loadtest.jmx

                echo ===== Echoing exact JMeter path =====
                echo "C:\\Program Files\\jmeter\\apache-jmeter-5.6.3\\bin\\jmeter.bat"

                echo ===== Dumping JMeter Test Plan Tree =====
                "C:\\Program Files\\jmeter\\apache-jmeter-5.6.3\\bin\\jmeter.bat" ^
                    -n ^
                    -t tests\\loadtest.jmx ^
                    -l NUL ^
                    -j jmeter_tree_dump.log ^
                    2>&1

                echo ===== Show JMeter log =====
                type jmeter_tree_dump.log
                '''
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
