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
        withMaven(
                maven: 'M3',
                mavenSettingsConfig: 'global') {

            // Run the maven build
            sh "mvn clean compile -B -V"
        }
    }

    stage('Test') {
        withMaven(
                maven: 'M3',
                mavenSettingsConfig: 'global') {

            // Run the maven build
            sh "mvn -Dmaven.test.failure.ignore org.jacoco:jacoco-maven-plugin:prepare-agent test -B -V"
        }
    }

    parallel 'Quality scan': {
        withMaven(
                maven: 'M3',
                mavenSettingsConfig: 'global') {

            // Run the maven build
            sh "mvn sonar:sonar -B -V"
        }
    }

    stage('Results') {
        step([$class: 'JacocoPublisher'])
        junit '**/target/surefire-reports/TEST-*.xml'
    }
}
