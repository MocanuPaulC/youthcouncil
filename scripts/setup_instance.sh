#!/bin/bash

# Author: Peter Buschenreiter
# Date: 2023-05-18
# Description: This script creates a vm with an enabled firewall and updates a duckdns domain.
#              It also creates a self-signed signed certificate for the domain using openssl.

name="integration-instance"
zone="europe-west1-b"
machine_type="e2-small"
image_project="ubuntu-os-cloud"
image_family="ubuntu-2204-lts"
tags="http-server,https-server"
project="integration-4-385315"
db_instance="integration-db"
domain="debattle"
service_account_email="gitlab-ci-cd@integration-4-385315.iam.gserviceaccount.com"
youthcouncil_service=$(cat scripts/youthcouncil.service)
password=$1
duckdns_token=$2


echo "Authenticating service account..."
gcloud auth activate-service-account --key-file="$(pwd)/key.json"
gcloud config set project "$project" --quiet

#check if sql instance already exists and create it if not
if ! gcloud sql instances list --quiet --format="value(name)" | grep -q "$db_instance"; then
    gcloud sql instances create "$db_instance" \
                        --database-version=POSTGRES_13 \
                        --cpu=1 \
                        --memory=3840MiB \
                        --zone="$zone" \
                        --root-password="$password"
fi

# if it exists check if stopped and start if so
if gcloud sql instances list --quiet --format="value(name)" | grep -q "$db_instance"; then
    if gcloud sql instances list --quiet --format="value(state)" | grep -q "RUNNABLE"; then
        echo "SQL Instance is already running."
    else
        gcloud sql instances patch "$db_instance" --zone=$zone --activation-policy=ALWAYS
    fi
fi

check-filter() {
    if [ -z "$1" ]; then
        echo "No tag provided"
        exit 1
    fi
    tag=$1
    rules=$(gcloud compute firewall-rules list --format="value(name)")

    default_port=80
    port=${2:-$default_port}

    default_protocol=tcp
    protocol=${3:-$default_protocol}

    for rule in $rules; do
        ruleTag=$(gcloud compute firewall-rules describe "$rule" --format='table[no-heading](targetTags)')
        if [[ $ruleTag =~ $tag ]]; then
            echo "Rule exists"
            return 0
        fi
    done

    gcloud compute firewall-rules create "allow-$tag" \
            --direction=INGRESS \
            --priority=1000 \
            --network=default \
            --action=ALLOW \
            --rules="$protocol":"$port" \
            --target-tags="$tag"
}

IFS=',' read -r -a tags_array <<< "$tags"
for tag in "${tags_array[@]}"; do
    if [ "$tag" == "https-server" ]; then
        check-filter "$tag" 443
    else
        check-filter "$tag"
    fi
done
unset IFS

#check if instance already exists and delete if so
if gcloud compute instances list --quiet --format="value(name)" | grep -q "$name"; then
    echo "Instance already exists. Deleting..."
    gcloud compute instances delete "$name" --zone=$zone -q
fi


gcloud compute instances create "$name" \
    --zone=$zone \
    --machine-type=$machine_type \
    --project=$project \
    --tags=$tags \
    --service-account=$service_account_email \
    --image-family=$image_family \
    --image-project=$image_project \
    --metadata=startup-script="#!/bin/bash
    apt-get update && apt install -y openjdk-17-jdk
    ufw allow 80/tcp
    ufw allow 22/tcp
    ufw allow 443/tcp
    ufw enable
    echo \"$youthcouncil_service\" > /etc/systemd/system/youthcouncil.service
    systemctl daemon-reload
    snap install core
    snap refresh core
    snap install --classic certbot
    ln -s /snap/bin/certbot /usr/bin/certbot
    curl -k \"https://www.duckdns.org/update?domains=$domain&token=$duckdns_token&ip=\""


echo "Waiting for VM to be ready..."
while ! gcloud compute ssh "$name" --zone="$zone" --command="echo 'VM is ready'" 2> /dev/null; do
    sleep 1
done

echo "Copying files to VM..."
gcloud compute scp --recurse build $name:~/ --zone $zone
gcloud compute scp --recurse key.json $name:~/key.json --zone $zone
gcloud compute scp --recurse .env $name:~/.env --zone $zone
echo "Files copied"

echo "Updating authorized networks..."
comp_instance_ip=$(gcloud compute instances describe "$name" --zone=$zone --format="value(networkInterfaces[0].accessConfigs[0].natIP)")
gcloud sql instances patch --quiet "$db_instance" --zone=$zone --authorized-networks="$comp_instance_ip"
echo "Authorized networks updated"

#echo "Enabling HTTPS..."
# CERTBOT WAY (ran out of tries, whoops)
#while ! gcloud compute ssh "$name" --zone="$zone" --command="certbot --version" 2> /dev/null; do
#    sleep 1
#done
#gcloud compute ssh "$name" --zone="$zone" --command="certbot certonly -n -a standalone -d $domain.duckdns.org -m $email --agree-tos"
#gcloud compute ssh "$name" --zone="$zone" --command="cd /etc/letsencrypt/live/$domain.duckdns.org && openssl pkcs12 -export -in fullchain.pem -inkey privkey.pem -out keystore.p12 -name tomcat -CAfile chain.pem -caname root -passout pass:$password"


echo "Waiting for Java to be ready..."
while ! gcloud compute ssh "$name" --zone="$zone" --command="java --version" 2> /dev/null; do
    sleep 1
done
echo "Java is ready"

while ! gcloud compute ssh "$name" --zone="$zone" --command="systemctl start youthcouncil.service" 2> /dev/null; do
    sleep 1
done

# Final command to run the jar in the background
#gcloud compute ssh "$name" --zone="$zone" --command="systemctl start youthcouncil.service"
