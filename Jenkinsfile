node {
    stage('download source')
    git 'https://github.com/gmdbmonolithrefactor/gmdb-monolithd.git'

    stage('gradle build and test')
    sh label: '', script: './gradlew build'

    stage('archiving jar file')
    archiveArtifacts 'build/libs/*.jar'
    publishHTML([allowMissing: true,
                 alwaysLinkToLastBuild: false,
                 keepAll: false,
                 reportDir: 'build/reports/tests/test/',
                 reportFiles: 'index.html',
                 reportName: 'UnitTest Results',
                 reportTitles: ''])

    stage('Generate test code coverage report')
    sh './gradlew jacocoTestReport'
    publishHTML([allowMissing: true,
                 alwaysLinkToLastBuild: false,
                 keepAll: false,
                 reportDir: 'build/reports/jacoco/test/html/',
                 reportFiles: 'index.html',
                 reportName: 'Code Coverage Results',
                 reportTitles: ''])
}
