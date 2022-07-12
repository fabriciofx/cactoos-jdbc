#!/bin/bash
mvn -Pqulice -Psonatype -Pcactoos-jdbc clean deploy -DskipTests=true --settings=../settings.xml
