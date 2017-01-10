#!groovy

node {
    def mvnHome
    stage('Preparation') {
        // Get the Maven tool.
        // ** NOTE: This 'M3' Maven tool must be configured
        // **       in the global configuration.
        mvnHome = tool 'M3'
    }
    stage('Build') {
        echo ${pwd}
        sh "'${mvnHome}/bin/mvn' clean compile"
        input 'Test?'
    }

    stage('Test') {
        // Run the maven build
        sh "'${mvnHome}/bin/mvn' -Dmaven.test.failure.ignore test"
    }
}
