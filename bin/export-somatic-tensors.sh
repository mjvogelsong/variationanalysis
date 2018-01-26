#!/usr/bin/env bash
FORCE_PLATFORM=native
. `dirname "${BASH_SOURCE[0]}"`/setup.sh

java -Xmx${memory_requirement} -cp ${DLVA_JAR}:${GDLVA_JAR} -Dlogback.configurationFile=${SLF4J_CONFIG} \
    org.campagnelab.dl.somatic.tools.ExportTensorsS ${other_parameters}