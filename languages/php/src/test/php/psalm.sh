#!/usr/bin/env bash
set -e
composer install
vendor/bin/psalm --threads=4
# find src -name "*.php" | xargs prettier --write
vendor/bin/php-cs-fixer fix --config=.php_cs.dist
