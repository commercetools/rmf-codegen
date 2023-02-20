#!/bin/bash

set -e

CLI_HOME=~/.rmf-cli
LIB_FOLDER=$CLI_HOME/lib
JAR_FILE_PATH=$LIB_FOLDER/codegen-cli.jar
SCRIPT_PATH=$CLI_HOME/rmf-cli.sh
COMMAND_SYM_LINK=/usr/local/bin/rmf-codegen

installRmfCli(){
    uninstallRmfCli
    mkdir -p $LIB_FOLDER
    echo 'Copying codegen...'
    cp rmf-codegen.jar $JAR_FILE_PATH
    cat >$SCRIPT_PATH <<EOL
#!/bin/sh

java -Dfile.encoding=UTF-8 -jar $JAR_FILE_PATH \$@
EOL
    chmod +x $SCRIPT_PATH
    ln -f $SCRIPT_PATH $COMMAND_SYM_LINK
    echo 'RMF codegen cli installed successfully'
}

uninstallRmfCli(){
    rm -rf $CLI_HOME
    rm -f $COMMAND_SYM_LINK
}

if ! [[ -f $JAR_FILE_PATH ]] || ! codegen_loc="$(type -p "rmf-codegen")" || [[ -z $codegen_loc ]] ; then
  installRmfCli
else
  INSTALLED_VERSION=`rmf-codegen -v`
  if [ "$CODEGEN_VERSION" != "$INSTALLED_VERSION" ]; then
    installRmfCli
  fi
fi
