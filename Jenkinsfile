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
                                    -Djib.to.image=${DOCKERHUB_USERNAME}/${service}:latest
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
                script {
                    // Create .env content with actual values
                    def envFileContent = """GOOGLE_CLIENT_ID=${env.GOOGLE_CLIENT_ID}
GOOGLE_CLIENT_SECRET=${env.GOOGLE_CLIENT_SECRET}
MAIL_USERNAME=nayanajith.ishara2541@gmail.com
MAIL_PASSWORD=ojwnzgvijvrslnod
DB_PASSWORD=root
"""
                    
                    // Write .env file locally
                    writeFile file: '.env.tmp', text: envFileContent
                    
                    sshagent(['vps-ssh']) {
                        // Copy .env file to VPS
                        sh """
                            scp -o StrictHostKeyChecking=no .env.tmp ${VPS_USER}@${VPS_IP}:/tmp/.env
                        """
                        
                        // Deploy on VPS
                        sh """
                            ssh -o StrictHostKeyChecking=no ${VPS_USER}@${VPS_IP} bash << 'ENDSSH'
set -e

echo "=== Stopping existing containers ==="
cd /opt/recruitment-system 2>/dev/null && sudo docker-compose down || echo "No existing containers to stop"

echo "=== Cleaning up old containers ==="
sudo docker rm -f \$(sudo docker ps -aq) 2>/dev/null || echo "No containers to remove"

echo "=== Setting up deployment directory ==="
sudo mkdir -p /opt/recruitment-system/prometheus
sudo mv /tmp/docker-compose.yml /opt/recruitment-system/
sudo mv /tmp/prometheus.yml /opt/recruitment-system/prometheus/

echo "=== Moving .env file ==="
sudo mv /tmp/.env /opt/recruitment-system/.env
sudo chmod 600 /opt/recruitment-system/.env

echo "=== Verifying .env file ==="
echo "Checking .env file contents (first 2 lines only for security):"
sudo head -n 2 /opt/recruitment-system/.env

echo "=== Changing to deployment directory ==="
cd /opt/recruitment-system

echo "=== Pulling latest images ==="
sudo docker-compose pull

echo "=== Starting services ==="
sudo docker-compose up -d

echo "=== Waiting for services ==="
sleep 30

echo "=== Service Status ==="
sudo docker-compose ps

echo "=== Checking auth-service environment ==="
sudo docker-compose exec -T auth-service env | grep -E "GOOGLE|MAIL|DB_PASSWORD" || echo "Environment variables not found"

echo "=== Recent auth-service Logs ==="
sudo docker-compose logs --tail=50 auth-service

echo "=== Deployment Complete ==="
ENDSSH
                        """
                    }
                    
                    // Clean up local temp file
                    sh 'rm -f .env.tmp'
                }
            }
        }
    }
    
    post {
        success {
            echo '✅ Pipeline completed successfully!'
            echo "🚀 Services deployed to ${VPS_IP}"
        }
        failure {
            echo '❌ Pipeline failed! Check console output for details.'
        }
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
    }
}