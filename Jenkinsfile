pipeline {
    // Définir l’agent Jenkins sur lequel s'exécute le pipeline
        // agent any

       agent {
            label 'agent-windows-ecommerce'
        }


        environment {
            // Définir des variables d’environnement pour l’image Docker
             DOCKERHUB_USER = "dialloousmane1999"
             BACKEND_IMAGE  = "${DOCKERHUB_USER}/backend-ecommerce"
             BACKEND_TAG    = "1.${BUILD_NUMBER}"

             // frontend
             FRONTEND_IMAGE = "${DOCKERHUB_USER}/frontend-ecommerce"
             FRONTEND_TAG = "1.${BUILD_NUMBER}"
             // Frontend  private repository
             FRONTEND_REPO_URL     = 'https://github.com/ousmanediallo902/frontend-ecommerce.git'
             FRONTEND_REPO_BRANCH  = 'main'
             FRONTEND_CREDENTIALS  = 'credential-id-github'


        }

         

                   tools {
                          jdk 'jdk17'
                    maven 'maven3'
                    // nodejs 'node20'
                        }

        stages {
            

           // checkout Backend
            stage('Checkout Backend Code') {

                steps {
                  dir('backend') {
                    // Récupérer le code source depuis le dépôt Git (SCM) ssddd
                    checkout scm
                  }
                }
            }



            stage('Compile Backend') {
    steps {
        dir('backend') {
            bat 'mvn clean install -DskipTests'
        }
    }
}

            // SonarQube Analysis - Backend
            stage('SonarQube Analysis - Backend') {
               steps {
                dir('backend') {
                     withSonarQubeEnv('sonar-scanner') {
                 bat 'mvn sonar:sonar -Dsonar.projectKey=backend-ecommerce -Dsonar.java.binaries=target/classes'

                 }
                  }
                }
                      }



             //  Checkout Frontend
             stage('Checkout Frontend') {
                 steps {
                     dir('frontend') {
                         git branch: env.FRONTEND_REPO_BRANCH,
                             url: env.FRONTEND_REPO_URL,
                             credentialsId: env.FRONTEND_CREDENTIALS
                     }
                 }
             }

             // 🔹 Analyse SonarQube Frontend
stage('Analyse SonarQube Frontend') {
    steps {
        dir('frontend') {
            withSonarQubeEnv('sonar-scanner') {
                withCredentials([string(credentialsId: 'token-sonar', variable: 'SONAR_AUTH_TOKEN')]) {
                    bat """
                        sonar-scanner ^
                          -Dsonar.projectKey=frontend-ecommerce ^
                          -Dsonar.projectName="frontend-ecommerce" ^
                          -Dsonar.sources=src ^
                          -Dsonar.host.url=http://localhost:9000 ^
                          -Dsonar.login=%SONAR_AUTH_TOKEN%
                    """
                }
            }
        }
    }
}


// OWASP Dependency-Check Frontend Analysis
stage('OWASP Dependency-Check Frontend') {
    steps {
        dir('frontend') {
            script {
                def depCheckHome = tool(
                    name: 'owasp-dependency-check',
                    type: 'org.jenkinsci.plugins.DependencyCheck.tools.DependencyCheckInstallation'
                )

                withCredentials([string(credentialsId: 'NVD_API_KEY', variable: 'NVD_API_KEY')]) {

                    bat """
                    if not exist reports mkdir reports

                    "${depCheckHome}\\bin\\dependency-check.bat" ^
                    --project "Frontend-NPM" ^
                    --scan . ^
                    --format HTML ^
                    --format XML ^
                    --out reports ^
                    --nvdApiKey %NVD_API_KEY%
                    """
                }
            }
        }
    }
    post {
        always {
            dependencyCheckPublisher pattern: 'frontend/reports/dependency-check-report.xml'

            archiveArtifacts artifacts: 'frontend/reports/dependency-check-report.html',
                             fingerprint: true
        }
    }
}


// OWASP Dependency-Check backend Analysis
stage('OWASP Dependency-Check Backend') {
    steps {
        dir('backend') {
            script {
                def depCheckHome = tool(
                    name: 'owasp-dependency-check',
                    type: 'org.jenkinsci.plugins.DependencyCheck.tools.DependencyCheckInstallation'
                )

                withCredentials([string(credentialsId: 'NVD_API_KEY', variable: 'NVD_API_KEY')]) {

                    bat """
                    if not exist reports mkdir reports

                    "${depCheckHome}\\bin\\dependency-check.bat" ^
                    --project "Backend-NPM" ^
                    --scan . ^
                    --format HTML ^
                    --format XML ^
                    --out reports ^
                    --nvdApiKey %NVD_API_KEY%
                    """
                }
            }
        }
    }
    post {
        always {
            dependencyCheckPublisher pattern: 'backend/reports/dependency-check-report.xml'

            archiveArtifacts artifacts: 'backend/reports/dependency-check-report.html',
                             fingerprint: true
        }
    }
}



            //  Build Backend Docker Image
            stage('Build Backend Docker Image') {
                 steps {
                   dir('backend'){
                     script {
                   bat "docker build -t %BACKEND_IMAGE%:%BACKEND_TAG% ."
                      }
                    }

                 }
            }


            //  Build Frontend Docker Image
                        stage('Build Frontend Docker Image') {
                             steps {
                               dir('frontend'){
                                 script {
                               bat "docker build -t %FRONTEND_IMAGE%:%FRONTEND_TAG% ."
                                  }
                                }

                             }
                        }


            // 🔹 Push images to DockerHub plateforme
                    stage('Push images to DockerHub') {
                        steps {
                            withCredentials([usernamePassword(credentialsId: 'dockerhub-credentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_PASS')]) {
                                bat """
                                    docker login -u %DOCKER_USER% -p %DOCKER_PASS%
                                    docker push %BACKEND_IMAGE%:%BACKEND_TAG%
                                    docker push %FRONTEND_IMAGE%:%FRONTEND_TAG%
                                    docker logout
                                """
                            }
                        }
                    }



            stage('Deploy with Docker Compose') {
                steps {
                    // Déployer les services avec Docker Compose en mode détaché
                    // bat "docker-compose down || true"
                    bat "docker-compose up -d --build"

                }
            }
        }

        post {
            success {
                // Afficher un message si le pipeline réussit
                echo " Déploiement réussi"
            }
            failure {
                // Afficher un message si le pipeline échoue
                echo " Le pipeline a échoué, vérifie les logs Jenkins."
            }



        }
}

//bbb
