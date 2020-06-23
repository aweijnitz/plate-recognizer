# Plate Parser

SpringBoot service that receives German license plate strings and parses and returns the associated region.

## Prerequisites

- Java/JDK installed (project developed using openjdk v13.0.2)
- Docker
- Minikube (should work with any Kubernetes implementation, including k3s)
- RabbitMQ: `docker run -it --rm --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management`
-- RabbitMQ Minikube guide https://github.com/rabbitmq/rabbitmq-peer-discovery-k8s/tree/master/examples
-- Admin at http://localhost:15672/ (guest/guest)

## Use

Running locally, without Docker or Minikube.

    $ java -jar target/application.jar
    $ curl http://localhost:9090/message
    
## Build and Build and Run

**NOTE!** To reduce the amount of configuration and adaptation that has to be done, it always builds an artifact named `target/application.jar`. This is not really best practice, but simplifies scripting. Just edit the pom.xml and get rid of `<finalName>application</finalName>` to revert back to normal behavior.
    
    $ mvn clean package
    $ mvn clean package spring-boot:run
    
## Build Docker Image

This project makes use of the new built-in docker buildpack in Spring Boot.
See https://spring.io/blog/2020/01/27/creating-docker-images-with-spring-boot-2-3-0-m1

    $ mvn spring-boot:build-image
    
    # Validate
    
    $ docker images
    REPOSITORY     TAG           IMAGE ID
    ...
    echoservice    1.0-SNAPSHOT  231e3123
    ...
    docker run -it -p8080:9090 echoservice:1.0-SNAPSHOT

## Run tests

    $ mvn test
    
## With Kubernetes on localhost

Remember, the local state can always be inspected using

    minikube dashboard
    
    # and (separate installation)
    k9s

### SETUP | Prerequisite

Change docker repositiory to Minikube and rebuild and push the image.

    $ eval $(minikube -p minikube docker-env)
    $ mvn spring-boot:build-image
    
### Installation

Convenience scrip: 

    cd ./k8s
    installInMinikube.sh

Step by step below

### Installing the application without the script

These same steps also works for updating the different resources

    $ kubectl apply -f echoservice-namespace.yml
    --> namespace/echoservice-ns created
    $ kubectl apply -f echoservice-deployment.yml
    --> deployment.apps/echoservice created
    $ kubectl apply -f echoservice-ingress.yml
    --> ingress.networking.k8s.io/echoservice-ingress created
    

### Exposing the application | Enable the backing Ingress Controller (only needed once)

The Loadbalancer service type is not available on localhost, so therefore we use an local Ingress controller.
See https://kubernetes.io/docs/tasks/access-application-cluster/ingress-minikube/


    # Enable ingress
    $ minikube addons enable ingress
    ## Verify
    $ minikube addons list
  
### Setting up the ingress service and mapping to the application 

    # Expose the deployment
    $ kubectl expose deployment echoservice --type=NodePort --port=9090 --namespace=echoservice-ns
    
    # Get the exposed URL
    $ minikube service echoservice --url --namespace=echoservice-ns
    ## --> http://192.168.64.3:31732
    
    # Verify
    $ curl http://192.168.64.3:31732/message
    ## --> {"id":1,"content":"Hello, World!"}
        
    # Apply service definition
    $ kubectl apply -f ./k8s/echoservice-ingress.yaml
    
    # Verify
    $ kubectl get ingress --namespace=echoservice-ns
    ## -->
    NAME                  CLASS    HOSTS   ADDRESS        PORTS   AGE
    echoservice-ingress   <none>   *       192.168.64.3   80      43m
    
    # Verify using ADDRESS from previous command
    $ curl http://192.168.64.3/message
    ## -->
    {"id":2,"content":"Hello, World!"}
 
