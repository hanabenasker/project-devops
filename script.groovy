def buildJar() {
    echo "building the application..."
    sh 'mvn clean install -Dmaven.test.skip=true'
}

def runUnitTests() {
    echo "running the unit tests..."
    sh 'mvn test'
}

def buildImage() {
    echo "building the docker image..."
    withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'PASS', usernameVariable: 'USER')]) {
        sh 'docker build -t hanabenasker/myrepo:devim .'
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push hanabenasker/myrepo:devim'
    }
}

def sonarTest() {
    echo "Running sonarQube checks..."
    sh 'mvn clean verify sonar:sonar   -Dmaven.test.skip=true  -Dsonar.projectKey=project-devops   -Dsonar.host.url=http://172.18.0.5:9000   -Dsonar.login=sqp_7bb89c97447e1f73a6bbc15fd71805a3e42af942'
}


def deployApp() {
    echo 'deploying the application...'
}

def pushToNexus() {
    /*
    echo "pushing the jar file to Nexus maven-snapshots repo..."
    sh 'mvn dependency:resolve'
    sh 'mvn clean deploy -Dmaven.test.skip=true'
*/
    nexusArtifactUploader artifacts: [[artifactId: 'project-devops', classifier: '', file: 'target/Uber.jar', type: 'jar']], credentialsId: 'nexus-auth', groupId: 'com.example', nexusUrl: 'localhost:8081', nexusVersion: 'nexus3', protocol: 'http', repository: 'repodev', version: '0.0.1-SNAPSHOT'
}


return this