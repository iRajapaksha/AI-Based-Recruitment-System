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
                    sh """
                        echo "=== Stopping existing containers ==="
                        sudo docker-compose down || echo "No existing containers to stop"

                        echo "=== Setting up deployment directory ==="
                        sudo mkdir -p /opt/recruitment-system/prometheus
                        
                        echo "=== Copying configuration files ==="
                        sudo cp docker-compose.yml /opt/recruitment-system/
                        sudo cp prometheus/prometheus.yml /opt/recruitment-system/prometheus/

                        echo "=== Changing to deployment directory ==="
                        cd /opt/recruitment-system

                        echo "=== Pulling latest images ==="
                        sudo docker-compose pull

                        echo "=== Starting services with Jenkins credentials ==="
                        sudo -E docker-compose up -d

                        echo "=== Waiting for services ==="
                        sleep 30

                        echo "=== Service Status ==="
                        sudo docker-compose ps

                        echo "=== Recent auth-service Logs ==="
                        sudo docker-compose logs --tail=50 auth-service

                        echo "=== Deployment Complete ==="
                    """
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