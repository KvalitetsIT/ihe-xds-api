#!/bin/bash

if docker pull kvalitetsit/ihe-xds-api-documentation:latest; then
    echo "Copy from old documentation image."
    docker cp $(docker create kvalitetsit/ihe-xds-api-documentation:latest):/usr/share/nginx/html target/old
fi
