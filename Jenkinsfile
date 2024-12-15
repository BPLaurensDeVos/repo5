pipeline {
    agent any

    tools {
        maven 'Maven3'
        jdk 'jdk17'
    }

    environment {
        GITHUB_ACTOR = credentials('GITHUB_ACTOR')
        GITHUB_TOKEN = credentials('PAT_TOKEN')
        SONAR_TOKEN = credentials('SONAR_TOKEN')
        REPO4_URL = "https://maven.pkg.github.com/BPLaurensDeVos/repo4/com/example/repo4/repo4/1.0/repo4-1.0.jar"
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Artifact Check & Trigger') {
            steps {
                script {
                    def statusCode = sh(
                        script: "curl -L -u ${GITHUB_ACTOR}:${GITHUB_TOKEN} -s -o /dev/null -w '%{http_code}' ${REPO4_URL}",
                        returnStdout: true
                    ).trim()

                    if (statusCode != '200') {
                        echo "Artifact missing. Triggering Repo4 Jenkins pipeline..."
                        build job: 'repo4', wait: true, propagate: true
                    } else {
                        echo "Artifact is available. Proceeding with build."
                    }
                }
            }
        }

        stage('Configure Maven') {
            steps {
                sh """
                mkdir -p ~/.m2
                echo "<settings xmlns='http://maven.apache.org/SETTINGS/1.0.0'
                      xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'
                      xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 https://maven.apache.org/xsd/settings-1.0.0.xsd'>
                    <servers>
                    <server>
                      <id>github-repo1</id>
                      <username>${GITHUB_ACTOR}</username>
                      <password>${GITHUB_TOKEN}</password>
                    </server>
                    <server>
                      <id>github-repo4</id>
                      <username>${GITHUB_ACTOR}</username>
                      <password>${GITHUB_TOKEN}</password>
                    </server>
                    <server>
                      <id>github-repo5</id>
                      <username>${GITHUB_ACTOR}</username>
                      <password>${GITHUB_TOKEN}</password>
                    </server>
                    </servers>
                  </settings>" > ~/.m2/settings.xml
                """
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn clean package'
            }
        }
        
        stage('SonarCloud Scan') {
            steps {
                withSonarQubeEnv('SonarCloud') {
                    sh 'mvn sonar:sonar'
                }
            }
        }

        stage('Deploy to GitHub Packages') {
            steps {
                sh 'mvn deploy'
            }
        }
    }

    post {
        success {
            echo 'Pipeline completed successfully.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}
