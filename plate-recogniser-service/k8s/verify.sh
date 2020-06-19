#!/bin/bash

echo "Verifying"
HOST=`kubectl get ingress --namespace=echoservice-ns | grep echoservice | awk '{print $4}'`
echo "Public Cluster IP is $HOST"
RESULT=`curl -k http://$HOST/message | grep -o Hello`
echo "$RESULT"
[ -z "$RESULT" ] && echo "TEST FAILED" || echo "TEST PASSED"
echo "Service expected available on http://$HOST/message"
