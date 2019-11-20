#!/usr/bin/env bash
set -e
composer install
vendor/bin/psalm --threads=4
# find src -name "*.php" | xargs prettier --write
vendor/bin/php-cs-fixer fix lib/commercetools-api/src --rules=@PhpCsFixer --using-cache=no
vendor/bin/php-cs-fixer fix lib/commercetools-import/src --rules=@PhpCsFixer --using-cache=no
vendor/bin/php-cs-fixer fix lib/commercetools-base/src --rules=@PhpCsFixer --using-cache=no
