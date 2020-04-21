#!/bin/bash

echo "Tagging yafred/asn1-playground"

git clone https://github.com/yafred/asn1-playground.git
cd asn1-playground
git tag -a $TRAVIS_TAG -m "Refresh docker image"
git push -f -q --follow-tags https://${GH_TOKEN}@github.com/yafred/asn1-playground master
