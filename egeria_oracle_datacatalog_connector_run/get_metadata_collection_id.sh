#!/bin/bash

grep setMetadataCollectionId odc-proxy.log | sed -e 's/^.*: setMetadataCollectionId(\(.*\))$/\1/'

