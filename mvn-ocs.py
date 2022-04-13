import sys
import os


# validate: validate the project is correct and all necessary information is available
# compile: compile the source code of the project
# test: test the compiled source code using a suitable unit testing framework. These tests should not require the code be packaged or deployed
# package: take the compiled code and package it in its distributable format, such as a JAR.
# integration-test: process and deploy the package if necessary into an environment where integration tests can be run
# verify: run any checks to verify the package is valid and meets quality criteria
# install: install the package into the local repository, for use as a dependency in other projects locally
# deploy: done in an integration or release environment, copies the final package to the remote repository for sharing with other developers and projects.
# clean: cleans up artifacts created by prior builds
# site: generates site documentation for this project


def run_maven_image(maven_args):

    local_maven_args = ''

    if maven_args:
        if maven_args in ['run', 'exec']:
            local_maven_args = 'clean compile exec:java'
        else:
            local_maven_args = maven_args
    else:
        local_maven_args = 'clean install'

    os.system('''podman run -it --rm --name my-maven-project -v "$(pwd)":/usr/src/mymaven -v "~/.m2":/tmp/m2 -w /usr/src/mymaven maven mvn -Dmaven.repo.local=/tmp/m2 ''' + local_maven_args)


argument_string = str(' '.join(sys.argv[1:]))

run_maven_image(argument_string)
