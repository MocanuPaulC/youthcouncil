#!/bin/bash

# Author: Peter Buschenreiter
# Date: 2023-05-18
# Description: This script destroys a vm and pauses the sql instance.

project="integration-4-385315"
name="integration-instance"
zone="europe-west1-b"


echo "Authenticating service account..."
gcloud auth activate-service-account --key-file="$(pwd)/key.json"
gcloud config set project "$project" --quiet


echo 'Destroying instance...'
gcloud compute instances delete "$name" --zone="$zone" --quiet

echo 'Pausing SQL instance...'
gcloud sql instances patch integration-db --activation-policy=NEVER --quiet
