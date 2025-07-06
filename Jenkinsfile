pipeline {
    agent any

    environment {
        DEPLOY_SERVER = 'user@your-server.com'       // Replace with your actual server
        DEPLOY_PATH = '/opt/shawarma-app'            // Replace with your desired path
        JAR_NAME = 'shawarma.jar'                    // Matches <finalName> in pom.xml
    }

    tools {
        maven 'Maven 3.8.6'                          // Make sure this matches your Jenkins Maven tool name
        jdk 'JDK 21'                                 // Or whatever version you're using
    }

    stages {
        stage('Checkout') {
            steps {
                git 'https://github.com/your-org/Shawarma.git' // Replace with your repo URL
            }
        }

        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }

        stage('Deploy') {
            steps {
                sshagent(['shawarma-ssh-key']) { // Replace with your actual Jenkins credential ID
                    sh """
                    echo "Copying JAR to remote server..."
                    scp target/${JAR_NAME} $DEPLOY_SERVER:$DEPLOY_PATH

                    echo "Restarting application on remote server..."
                    ssh $DEPLOY_SERVER '
                        pkill -f ${JAR_NAME} || true
                        nohup java -jar $DEPLOY_PATH/${JAR_NAME} > /dev/null 2>&1 &
                    '
                    """
                }
            }
        }
    }

    post {
        success {
            echo '✅ Deployment successful!'
        }
        failure {
            echo '❌ Build or deployment failed.'
        }
    }
}
