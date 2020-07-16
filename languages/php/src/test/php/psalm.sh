#!/usr/bin/env bash
set -e
composer install
vendor/bin/psalm --threads=6
time ./prettify.sh
vendor/bin/psalm --threads=6
