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
            sh "'${mvnHome}/bin/mvn' clean compile -B -V"
        }
    }

    stage('Test') {
        // Run the maven build
        sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore org.jacoco:jacoco-maven-plugin:prepare-agent test -B -V"
    }

    stage('Results') {
        step([$class: 'JacocoPublisher'])
        junit '**/target/surefire-reports/TEST-*.xml'
    }
}
