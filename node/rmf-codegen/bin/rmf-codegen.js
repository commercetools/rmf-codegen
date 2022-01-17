#! /usr/bin/env node

require('../src/run').run(process.argv.slice(2)).on('close', (exitCode) => { process.exit(exitCode) })
