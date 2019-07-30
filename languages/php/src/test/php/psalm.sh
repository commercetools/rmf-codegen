#!/usr/bin/env bash
set -e
composer install
vendor/bin/psalm --threads=4
find src -name "*.php" | xargs prettier --write
php-cs-fixer fix src --rules=@PhpCsFixer
