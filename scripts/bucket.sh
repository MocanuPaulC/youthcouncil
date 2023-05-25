#!/bin/bash
e_h="[ERROR]  : "
i_h="[INFO]   : "
p_h="[PROMPT] : "


function create_bucket {
    test $# = "2" || ( printf "${e_h}Please pass 2 parameters to the %s function!" $0 && exit 1 )
    local bucket_name=$1
    local id=$2

    gsutil mb gs://${bucket_name}/ || (printf "${e_h}Bucket creation failed" && exit 2)
    gcloud iam service-accounts create ${bucket_name} --display-name=${bucket_name} || (printf "${e_h}Service account creation failed" && exit 2)
    gsutil acl ch -u ${bucket_name}@${id}.iam.gserviceaccount.com:O gs://${bucket_name} || (printf "${e_h}Permission creation for service account failed" && exit 2)
    gsutil defacl ch -u ${bucket_name}@${id}.iam.gserviceaccount.com:O gs://${bucket_name} || (printf "${e_h}Setting of default access to bucket failed" && exit 2)
    gsutil defacl ch -u AllUsers:R gs://${bucket_name} || (printf "${e_h}Permission creation for all users failed" && exit 2)
    gcloud iam service-accounts keys create ../src/main/resources/${bucket_name}.json --iam-account=${bucket_name}@${id}.iam.gserviceaccount.com --key-file-type=json || (printf "${e_h}Bucket key file creation failed" && exit 2)
    printf "\n${i_h}Creation complete, the json file associated with this account\n\
    should be available in the directory of this file."
}

clear
printf "${i_h}Please make sure to run this in the scripts folder of the java project!\n"

printf "${i_h}Creating bucket, just press enter to use default values in the square brackets.\n"
echo -n "${p_h}Enter a name that will be used for the service account and bucket [int4-team4-bucket]: "
read -r bucket_name
echo -n "${p_h}Enter your user id on gcloud [integration-4-385315]: "
read -r id

create_bucket bucket_name id || (printf "${e_h}Bucket creation failed!" && exit 1 )
