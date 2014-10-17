#!/bin/sh
#grails -Dserver.port=8083 run-app
grails -noreloading -Dserver.port=8083 run-app | tee cmis.log
