#!groovy

node {
    def mvnHome
    def workspace
    def pom
    def version

    stage('Checkout') {
        checkout scm
    }

    stage('Preparation') {
        // Get the Maven tool.
        // ** NOTE: This 'M3' Maven tool must be configured
        // **       in the global configuration.
        mvnHome = tool 'M3'
        workspace = pwd
        pom = readMavenPom file: 'pom.xml'
        version = pom.version.replace("-SNAPSHOT", ".${currentBuild.number}")
    }

    stage('Build') {
        dir(env.WORKSPACE) {
            sh "pwd && '${mvnHome}/bin/mvn' clean compile"
        }
        input 'Test?'
    }

    stage('Test') {
        // Run the maven build
        sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore test"
    }

    stage('Results') {
        junit '**/target/surefire-reports/TEST-*.xml'
    }
}
