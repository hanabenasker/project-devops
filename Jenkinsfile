#!/usr/bin/env groovy
def gv

pipeline {
    agent any
    tools {
        maven 'maven'
    }
    stages {
        stage("init") {
            steps {
                script {
                    gv = load "script.groovy"
                }
            }
        }
        // stage('increment version') {
        //     steps {
        //         script {
        //             echo 'incrementing app version...'
        //             sh 'mvn build-helper:parse-version versions:set \
        //                 -DnewVersion=\\\${parsedVersion.majorVersion}.\\\${parsedVersion.minorVersion}.\\\${parsedVersion.nextIncrementalVersion} \
        //                 versions:commit'
        //             def matcher = readFile('pom.xml') =~ '<version>(.+)</version>'
        //             def version = matcher[0][1]
        //             env.IMAGE_NAME = "$version-$BUILD_NUMBER"
        //         }
        //     }
        // }

        stage('run unit tests') {
            steps {
                sh "mvn test"
            }
        }

        stage('test with sonar') {
            steps {
                script {
                    gv.sonarTest()
                }
            }
        }
        
        stage('build app') {
            steps {
                script {
                    echo "building jar"
                    gv.buildJar()
                }
            }
        }

        stage('push to nexus') {
            steps {
                script {
                    echo "pushing jar to nexus"
                    // gv.pushToNexus()
                }
            }
        }

        stage('build image') {
            steps {
                script {
                     echo "building image"
                    gv.buildImage()
                }
            }
        }
        stage('deploy') {
            steps {
                script {
                    echo 'deploying the application...'
                }
            }
        }

    }
}
