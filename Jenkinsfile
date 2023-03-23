pipeline {
  agent any
  environment {
    DH_CREDS=credentials('dh-creds')
    IMAGE_NAME="darinpope/my-rife2-app"
    IMAGE_VERSION="0.1.0"
    FLY_API_TOKEN=credentials('fly-api-token')
    FLY_APP="my-rife2-app"
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
    stage('jar') {
      steps {
        sh './bld jar'
      }
    }
    stage('Create the image') {
      steps {
        sh 'docker build --progress=plain -t $IMAGE_NAME:$IMAGE_VERSION .'
      }
    }  
    stage('grype') {
      steps {
        sh 'grype $IMAGE_NAME:$IMAGE_VERSION --scope AllLayers'
      }
    }
    stage('trivy') {
      steps {
        sh 'trivy $IMAGE_NAME:$IMAGE_VERSION'
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
        sh 'flyctl regions set iad'
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
      sh 'docker logout'
    }
  }  
}