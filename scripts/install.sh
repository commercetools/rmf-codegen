#!/bin/bash

set -e

CODEGEN_VERSION=${VRAP_VERSION:-"1.0.0-20201111123528"}
CLI_HOME=~/.vrap-cli
LIB_FOLDER=$CLI_HOME/lib
JAR_FILE_PATH=$LIB_FOLDER/codegen-cli-${CODEGEN_VERSION}.jar
SCRIPT_PATH=$CLI_HOME/vrap.sh
DOWNLOAD_LINK=https://dl.bintray.com/vrapio/vrapio/io/vrap/rmf/codegen/cli-application/${CODEGEN_VERSION}/cli-application-${CODEGEN_VERSION}-all.jar
COMMAND_SYM_LINK=/usr/local/bin/rmf-codegen

installVrapCli(){
    uninstallVrapCli
    mkdir -p $LIB_FOLDER
    echo 'Downloading artifacts...'
    curl -# -L $DOWNLOAD_LINK -o $JAR_FILE_PATH --fail
    cat >$SCRIPT_PATH <<EOL
#!/bin/sh

java -jar $JAR_FILE_PATH \$@
EOL
    chmod +x $SCRIPT_PATH
    ln -f $SCRIPT_PATH $COMMAND_SYM_LINK
    echo 'Vrap cli installed successfully'
}

uninstallVrapCli(){
    rm -rf $CLI_HOME
    rm -f $COMMAND_SYM_LINK
}

installVrapCli
