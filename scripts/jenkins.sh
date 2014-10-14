export PATH=/usr/bin/env:/var/lib/jenkins/tools/jenkins.plugins.nodejs.tools.NodeJSInstallation/nodejs/bin:/data/supplier-submission/play-1.2.4/:$PATH

# Get the content
git submodule init && git submodule update

# Install node things
npm cache clean
npm install

# Generate the content as a properties file
node run_grunt.js content

# Run Jasmine tests of client side code
node run_grunt.js dev
node run_grunt.js test

# Generate assets into public
node run_grunt.js production

# Run unit and Selenium tests within play
./scripts/updateEnvironment.sh ssp-preview ${ENCRYPTION_KEY} false
play clean
play deps
play auto-test

# Build artefact for staging
./scripts/updateEnvironment.sh ssp-staging ${ENCRYPTION_KEY} false
play clean
play deps
play war -o /data/supplier-submission/last-successful/submissions-staging-${BUILD_NUMBER}.war

# Build artefact for live
./scripts/updateEnvironment.sh ssp-live ${ENCRYPTION_KEY} true
play clean
play deps
play war -o /data/supplier-submission/last-successful/submissions-live-${BUILD_NUMBER}.war

git tag -a jenkins-build-${BUILD_NUMBER} -m "To deploy this build, use the following build number in Jenkins: ${BUILD_NUMBER}"
git push --tags