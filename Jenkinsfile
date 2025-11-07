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
                        // 'interview-service',
                        // 'transcript-service'
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
        
        stage('Deploy to VPS') {
            when {
                branch 'main'
            }
            steps {
                echo 'Deploying to VPS...'
                sshagent(['vps-ssh-key']) {
                    sh """
                        ssh -o StrictHostKeyChecking=no ubuntu@${VPS_IP} << 'ENDSSH'
                        cd /opt/recruitment-system
                        
                        # Export environment variables
                        export GOOGLE_CLIENT_ID="${GOOGLE_CLIENT_ID}"
                        export GOOGLE_CLIENT_SECRET="${GOOGLE_CLIENT_SECRET}"
                        
                        # Pull latest images
                        sudo docker-compose pull
                        
                        # Restart services
                        sudo docker-compose down
                        sudo docker-compose up -d
                        
                        # Show status
                        sudo docker-compose ps
                        
                        # Show logs for verification
                        sudo docker-compose logs --tail=50
ENDSSH
                    """
                }
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
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