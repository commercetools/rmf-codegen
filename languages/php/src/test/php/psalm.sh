#!/usr/bin/env bash
set -e
composer install
vendor/bin/psalm --threads=4
time ./prettify.sh
vendor/bin/psalm --threads=4
