#!/bin/bash

set -e

readonly APP_NAME=$1
readonly IMAGE=$2
readonly HEROKU_API_KEY=$3

echo "Deploying $IMAGE to $APP_NAME"

docker login --username=_ --password=$HEROKU_API_KEY registry.heroku.com

docker tag $IMAGE registry.heroku.com/$APP_NAME/web

docker push registry.heroku.com/$APP_NAME/web

curl -f -n -X PATCH -H "Authorization: Bearer $HEROKU_API_KEY" https://api.heroku.com/apps/$APP_NAME/formation \
  -d '{
  "updates": [
    {
      "type": "web",
      "docker_image": "'"$(docker inspect registry.heroku.com/$APP_NAME/web --format={{.Id}})"'"
    }
  ]
}' \
  -H "Content-Type: application/json" \
-H "Accept: application/vnd.heroku+json; version=3.docker-releases"
