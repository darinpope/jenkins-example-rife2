pipeline {
  agent any
  environment {
    DH_CREDS=credentials('dh-creds')
    IMAGE_NAME="darinpope/my-rife2-app"
    IMAGE_VERSION="0.1.0"
    FLY_API_TOKEN=credentials('fly-api-token')
    FLY_APP="my-rifetwo-app"
  } 
  options {
    buildDiscarder logRotator(artifactDaysToKeepStr: '', artifactNumToKeepStr: '', daysToKeepStr: '', numToKeepStr: '3')
  }  
  stages {
    stage('verify tooling') {
      steps {
        sh '''
          java -version
          ./bld version
          flyctl version
          grype version
          trivy version
          docker info
        '''
      }
    }
    stage('download') {
      steps {
        sh './bld download'
      }
    }
    stage('compile') {
      steps {
        sh './bld clean compile'
      }
    }
    stage('precompile') {
      steps {
        sh './bld precompile'
      }
    }
    stage('test') {
      steps {
        sh './bld test'
      }
    }
    stage('uberjar') {
      steps {
        sh './bld uberjar'
      }
    }
    stage('Create the image') {
      steps {
        sh 'docker build --progress=plain -t $IMAGE_NAME:$IMAGE_VERSION .'
      }
    }  
    stage('grype') {
      steps {
        sh 'grype $IMAGE_NAME:$IMAGE_VERSION --scope AllLayers --fail-on negligible'
      }
    }
    stage('trivy') {
      steps {
        sh 'trivy image --exit-code 1 $IMAGE_NAME:$IMAGE_VERSION'
      }
    }
    stage('login') {
      steps {
        sh 'echo $DH_CREDS_PSW | docker login --username=$DH_CREDS_USR --password-stdin'
      }
    }  
    stage('push image') {
      steps {
        sh 'docker push $IMAGE_NAME:$IMAGE_VERSION'
      }
    }
    stage('deploy to fly') {
      steps {
        sh 'flyctl regions set mia'
        sh 'flyctl deploy --image $IMAGE_NAME:$IMAGE_VERSION --now'
      }
    }
    stage('verify deployment') {
      steps {
        sh 'flyctl status'
      }
    }    
  }
  post {
    always {
      sh '''
        docker rmi $IMAGE_NAME:$IMAGE_VERSION
        docker logout
      '''
      cleanWs()
    }
  }  
}