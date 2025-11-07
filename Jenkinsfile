pipeline {
    agent any
    
    tools {
        maven 'Maven-3.9'
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
                        'resume-service'
                    ]
                    
                    services.each { service ->
                        stage("Build ${service}") {
                            echo "Building ${service}..."
                            dir(service) {
                                bat """
                                    mvn clean compile jib:build ^
                                    -Djib.to.auth.username=%DOCKER_HUB_CREDENTIALS_USR% ^
                                    -Djib.to.auth.password=%DOCKER_HUB_CREDENTIALS_PSW% ^
                                    -Djib.to.image=${DOCKERHUB_USERNAME}/${service}:latest ^
                                    -DGOOGLE_CLIENT_ID=%GOOGLE_CLIENT_ID% ^
                                    -DGOOGLE_CLIENT_SECRET=%GOOGLE_CLIENT_SECRET%
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
                    bat """
                        ssh -o StrictHostKeyChecking=no username@your-vps-ip "cd /opt/recruitment-system && docker-compose pull && docker-compose down && docker-compose up -d"
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
            echo 'Pipeline failed!'
        }
        always {
            echo 'Cleaning up workspace...'
            cleanWs()
        }
    }
}