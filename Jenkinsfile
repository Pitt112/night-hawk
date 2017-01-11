#!groovy

node {
    def mvnHome
    def workspace

    stage('Preparation') {
        // Get the Maven tool.
        // ** NOTE: This 'M3' Maven tool must be configured
        // **       in the global configuration.
        mvnHome = tool 'M3'
        workspace = pwd
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
}
