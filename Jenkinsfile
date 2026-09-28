pipeline {

    agent any

    environment {
        PATH = "/opt/homebrew/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"
    }

    stages {

        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean compile'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

        stage('Secret Scan') {
            steps {
                sh '''
                    echo "Running Trivy secret scan..."

                    trivy fs \
                      --scanners secret \
                      --skip-dirs target \
                      --exit-code 1 \
                      .
                '''
            }
        }

        stage('Verify Docker') {
            steps {
                sh '''
                    echo "Docker location:"
                    which docker

                    echo "Docker version:"
                    docker --version
                '''
            }
        }

        stage('Docker Build') {
            steps {
                sh '''
                    docker build \
                    -t civicpulse-grievance-service:${BUILD_NUMBER} .
                '''
            }
        }
    }

    post {
        success {
            echo 'GrievanceService CI pipeline completed successfully.'
        }

        failure {
            echo 'GrievanceService CI pipeline failed.'
        }

        always {
            echo "Build Number: ${BUILD_NUMBER}"
        }
    }
}