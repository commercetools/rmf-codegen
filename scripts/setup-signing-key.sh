#!/bin/bash

set -e

# Decrypt credentials
echo ${DECRYPTER} | base64 --decode > decrypter.json
echo ${SIGNING_KEY} | base64 --decode > signing_key.enc
echo ${PASSPHRASE} | base64 --decode > signing_passphrase.enc

gcloud auth activate-service-account --key-file decrypter.json

echo "Decrypt signing secrets"
set -x
gcloud kms decrypt \
  --project=commercetools-platform \
  --location=global \
  --keyring=teamcity \
  --key=jvm-sdk \
  --ciphertext-file=signing_key.enc \
  --plaintext-file=signing_key.asc

gcloud kms decrypt \
  --project=commercetools-platform \
  --location=global \
  --keyring=teamcity \
  --key=jvm-sdk \
  --ciphertext-file=signing_passphrase.enc \
  --plaintext-file=signing_passphrase.txt

# Import the GPG key
set +e
echo "Importing the signing key"
gpg --import --no-tty --batch --yes signing_key.asc
set -e

# List available GPG keys
gpg -K

echo "PASSPHRASETXT=`cat signing_passphrase.txt`" >> $GITHUB_ENV
rm signing_passphrase.txt

export GITHUB_ENV
