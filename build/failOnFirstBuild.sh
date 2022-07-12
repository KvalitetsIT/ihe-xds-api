#!/bin/sh

echo "${GITHUB_REPOSITORY}"
echo "${DOCKER_SERVICE}"
if [ "${GITHUB_REPOSITORY}" != "KvalitetsIT/ihe-xds-api" ] && [ "${DOCKER_SERVICE}" = "kvalitetsit/ihe-xds-api" ]; then
  echo "Please run setup.sh REPOSITORY_NAME"
  exit 1
fi
