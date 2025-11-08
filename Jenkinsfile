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
        VPS_IP = '15.235.210.227'
        VPS_USER = 'ubuntu'
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
                                    -Djib.to.image=${DOCKERHUB_USERNAME}/${service}:latest \
                                    -DGOOGLE_CLIENT_ID=\${GOOGLE_CLIENT_ID} \
                                    -DGOOGLE_CLIENT_SECRET=\${GOOGLE_CLIENT_SECRET}
                                """
                            }
                        }
                    }
                }
            }
        }
        
        stage('Copy Configuration to VPS') {
            steps {
                echo 'Copying docker-compose.yml and prometheus.yml to VPS...'
                sshagent(['vps-ssh']) {
                    sh """
                        scp -o StrictHostKeyChecking=no docker-compose.yml ${VPS_USER}@${VPS_IP}:/tmp/
                        scp -o StrictHostKeyChecking=no prometheus/prometheus.yml ${VPS_USER}@${VPS_IP}:/tmp/
                    """
                }
            }
        }
        
        stage('Deploy to VPS') {
            steps {
                echo 'Deploying to VPS...'
                sshagent(['vps-ssh']) {
                    sh '''
                        ssh -o StrictHostKeyChecking=no ${VPS_USER}@${VPS_IP} bash -s << 'ENDSSH'
set -e

echo "=== Stopping existing containers ==="
cd /opt/recruitment-system 2>/dev/null && sudo docker-compose down || echo "No existing containers to stop"

echo "=== Cleaning up old containers ==="
sudo docker rm -f $(sudo docker ps -aq) 2>/dev/null || echo "No containers to remove"

echo "=== Setting up deployment directory ==="
sudo mkdir -p /opt/recruitment-system/prometheus
sudo mv /tmp/docker-compose.yml /opt/recruitment-system/
sudo mv /tmp/prometheus.yml /opt/recruitment-system/prometheus/

echo "=== Creating .env file ==="
sudo tee /opt/recruitment-system/.env > /dev/null << EOF
GOOGLE_CLIENT_ID=${GOOGLE_CLIENT_ID}
GOOGLE_CLIENT_SECRET=${GOOGLE_CLIENT_SECRET}
EOF

echo "=== Changing to deployment directory ==="
cd /opt/recruitment-system

echo "=== Pulling latest images ==="
sudo docker-compose pull

echo "=== Starting services ==="
sudo docker-compose up -d

echo "=== Waiting for services ==="
sleep 20

echo "=== Service Status ==="
sudo docker-compose ps

echo "=== Recent Logs ==="
sudo docker-compose logs --tail=30

echo "=== Deployment Complete ==="
ENDSSH
                    '''
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
            echo "Services deployed to ${VPS_IP}"
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