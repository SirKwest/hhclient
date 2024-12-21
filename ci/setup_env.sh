#!/bin/bash

envs="
hhAccessToken=${GH_HH_ACCESS_TOKEN}
userAgent=${GH_HH_USER_AGENT}
"

(echo "$envs" | grep -E '.+=.+') >> develop.properties
