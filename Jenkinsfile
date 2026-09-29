pipeline {

    agent any

    environment {
        PATH = "/opt/homebrew/bin:/usr/local/bin:/usr/bin:/bin:/usr/sbin:/sbin"
        DOCKERHUB_USERNAME = "illuriganesh123"
        SPRING_DATASOURCE_PASSWORD = credentials('civicpulse-db-password')
        AZURE_STORAGE_CONNECTION_STRING = credentials('civicpulse-azure-connection')
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
        stage('Docker Image Scan') {
            steps {
                sh '''
                    echo "Scanning Docker image for vulnerabilities..."
        
                    trivy image \
                      --severity HIGH,CRITICAL \
                      civicpulse-grievance-service:${BUILD_NUMBER}
                '''
            }
        }
        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub-civicpulse',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login \
                            -u "$DOCKER_USERNAME" \
                            --password-stdin
        
                        docker tag \
                            civicpulse-grievance-service:${BUILD_NUMBER} \
                            ${DOCKER_USERNAME}/civicpulse-grievance-service:${BUILD_NUMBER}
        
                        docker push \
                            ${DOCKER_USERNAME}/civicpulse-grievance-service:${BUILD_NUMBER}
        
                        docker logout
                    '''
                }
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
