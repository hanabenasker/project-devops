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
    withCredentials([usernamePassword(credentialsId: 'docker-credentials', passwordVariable: 'b@hnJ7uFP3csPz2', usernameVariable: 'hanabenasker')]) {
        sh 'docker build -t hanaghz/myrepo:${IMAGE_NAME} .'
        sh "echo $PASS | docker login -u $USER --password-stdin"
        sh 'docker push hanabenasker/myrepo:${IMAGE_NAME}'
    }
}

def sonarTest() {
    echo "Running sonarQube checks..."
    sh 'mvn clean verify sonar:sonar   -Dmaven.test.skip=true  -Dsonar.projectKey=project-devops   -Dsonar.host.url=http://172.18.0.6:9000   -Dsonar.login=sqp_7bb89c97447e1f73a6bbc15fd71805a3e42af942'
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