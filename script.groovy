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
    withCredentials([usernamePassword(credentialsId: 'dockerhub', passwordVariable: 'b@hnJ7uFP3csPz2', usernameVariable: 'hanabenasker')]) {
        sh 'docker build -t hanabenasker/myrepo:""$BUILD_ID"" .'
        sh "echo 'b@hnJ7uFP3csPz2'| docker login -u 'hanabenasker' --password-stdin"
        sh 'docker push hanabenasker/myrepo:""$BUILD_ID""'
    }
}

def sonarTest() {
    echo "Running sonarQube checks..."
    sh 'mvn clean verify sonar:sonar   -Dmaven.test.skip=true  -Dsonar.projectKey=project-devops   -Dsonar.host.url=http://172.18.0.4:9000   -Dsonar.login=sqp_7bb89c97447e1f73a6bbc15fd71805a3e42af942'
}


def deployApp() {
    echo 'deploying the application...'
}

def pushToNexus() {
    echo "pushing the jar file to Nexus maven-snapshots repo..."
    sh 'mvn dependency:resolve'
    sh 'mvn clean deploy -Dmaven.test.skip=true'
}


return this