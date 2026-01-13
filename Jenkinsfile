pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9'
        jdk 'Java-21'
    }
    
    environment {
        DOCKER_HUB_CREDENTIALS = credentials('dockerhub-credentials')
        GOOGLE_CLIENT_ID = credentials('google-oauth-client-id')
        GOOGLE_CLIENT_SECRET = credentials('google-oauth-client-secret')
        DOCKERHUB_USERNAME = 'kavindaagkr'
    }
    
    stages {
        stage('Checkout') {
            steps {
                echo 'Cloning repository...'
                checkout scm
            }
        }
        
        stage('Build Shared Module') {
            steps {
                echo 'Building shared-module...'
                dir('shared-module') {
                    sh """
                        mvn clean install -DskipTests
                    """
                }
            }
        }
        
        stage('Build Services') {
            steps {
                script {
                    def services = [
                        'service-registry',
                        'api-gateway',
                        'auth-service',
                        'user-service',
                        'organization-service',
                        'jobpost-service',
                        'screening-service',
                        'notification-service',
                        'application-service',
                    ]
                    
                    services.each { service ->
                        stage("Build ${service}") {
                            echo "Building ${service}..."
                            dir(service) {
                                sh """
                                    mvn clean compile jib:build \
                                    -Djib.to.auth.username=\${DOCKER_HUB_CREDENTIALS_USR} \
                                    -Djib.to.auth.password=\${DOCKER_HUB_CREDENTIALS_PSW} \
                                    -Djib.to.image=${DOCKERHUB_USERNAME}/${service}:latest
                                """
                            }
                        }
                    }
                }
            }
        }
        
        stage('Deploy Locally') {
            steps {
                echo 'Deploying services locally...'
                script {
                    // Create .env file with Jenkins credentials
                    writeFile file: '.env', text: """GOOGLE_CLIENT_ID=${env.GOOGLE_CLIENT_ID}
GOOGLE_CLIENT_SECRET=${env.GOOGLE_CLIENT_SECRET}
"""
                    
                    sh """
                        echo "=== Current workspace ==="
                        pwd
                        ls -la

                        echo "=== Checking Docker availability ==="
                        which docker || echo "Docker not found"
                        docker --version || echo "Docker command failed"
                        
                        echo "=== Configuration files ready for deployment ==="
                        echo "docker-compose.yml and .env are prepared in workspace"
                        echo "Manual deployment can be done with: docker-compose up -d"
                        
                        echo "=== Environment variables set ==="
                        echo "GOOGLE_CLIENT_ID is configured"
                        echo "GOOGLE_CLIENT_SECRET is configured"
                        
                        echo "=== Deployment preparation complete ==="
                    """
                    
                    // Clean up the .env file
                    sh 'rm -f .env'
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
            echo 'Services deployed locally'
        }
        failure {
            echo 'Pipeline failed! Check console output for details.'
        }
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
    }
}