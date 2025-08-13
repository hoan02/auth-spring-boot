pipeline {
    agent {
        kubernetes {
            yaml """
apiVersion: v1
kind: Pod
spec:
  containers:
  - name: maven
    image: maven:3.8.6-openjdk-17-slim
    command:
    - cat
    tty: true
    volumeMounts:
      - name: maven-cache
        mountPath: /root/.m2
  - name: kaniko
    image: gcr.io/kaniko-project/executor:latest
    command:
    - cat
    tty: true
    volumeMounts:
      - name: kaniko-secret
        mountPath: /kaniko/.docker
  - name: kubectl
    image: bitnami/kubectl:latest
    command:
    - cat
    tty: true
  volumes:
    - name: maven-cache
      persistentVolumeClaim:
        claimName: maven-cache-pvc
    - name: kaniko-secret
      secret:
        secretName: registry-secret
        items:
          - key: .dockerconfigjson
            path: config.json
"""
        }
    }
    
    environment {
        REGISTRY = 'registry.container-registry.svc.cluster.local:5000'
        IMAGE_NAME = 'auth-service'
        NAMESPACE = 'auth-service'
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build & Test') {
            steps {
                container('maven') {
                    sh 'mvn clean compile test'
                }
            }
        }
        
        stage('Package') {
            steps {
                container('maven') {
                    sh 'mvn package -DskipTests'
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                container('kaniko') {
                    script {
                        def imageTag = "${REGISTRY}/${IMAGE_NAME}:${BUILD_NUMBER}"
                        sh """
                            /kaniko/executor \
                            --context=dir://. \
                            --dockerfile=Dockerfile \
                            --destination=${imageTag} \
                            --destination=${REGISTRY}/${IMAGE_NAME}:latest \
                            --insecure \
                            --skip-tls-verify
                        """
                    }
                }
            }
        }
        
        stage('Deploy to MicroK8s') {
            steps {
                container('kubectl') {
                    script {
                        sh """
                            kubectl apply -f k8s/namespace.yaml
                            kubectl apply -f k8s/secret.yaml
                            kubectl apply -f k8s/configmap.yaml
                            kubectl apply -f k8s/deployment.yaml
                            kubectl apply -f k8s/service.yaml
                            kubectl apply -f k8s/ingress.yaml
                            
                            kubectl rollout restart deployment/auth-service -n ${NAMESPACE}
                            kubectl rollout status deployment/auth-service -n ${NAMESPACE} --timeout=300s
                        """
                    }
                }
            }
        }
        
        stage('Health Check') {
            steps {
                container('kubectl') {
                    script {
                        sh """
                            kubectl get pods -n ${NAMESPACE}
                            kubectl get svc -n ${NAMESPACE}
                        """
                    }
                }
            }
        }
    }
    
    post {
        always {
            cleanWs()
        }
        success {
            echo 'Deployment successful!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}
