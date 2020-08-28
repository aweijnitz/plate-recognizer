# plate-recognizer
Extract vehicle license plates from images and classify which region they come from, based on license plate prefix.

Overview and Architecture https://docs.google.com/presentation/d/1w4pLd91MIKcl-y6eHqbg5SXEbpdhrmvtSX30kj_kXIQ/edit?usp=sharing

## STATUS OF THIS PROJECT

This is very much a classic side project that was deveoped to a point where it works on my local machine, but then work duties took over and I had to put it to the side.

If you run Linux or Mac, it should be stright forward to get it going, but the documenatation is still rough and thre are some obvious bugs, if you start using it more than casually.

## Install and start

## Pre-requisites

- Docker
- Java14
- Maven
- Node.js (sse webapp/README.md)

## Run locally

You need to start all the services, one by one, starting with RabbitMQ. 

    cd rabbitMQ && ./runDocker.sh
    cd plate-recogniser && ./buildAndRunMac.sh
    cd plate-parser && ./buildAndRun.sh
    cd webAPI && ./buildAndRun.sh
    cd webapp && ./runLocal.sh  ## See README on how to setup

There are two web pages:

- The camera UI: http://localhost:8000/
- The file upload: http://localhost:8000/upload.html

## Develop

Each service is it's own project, so deveopment is basically your normal SpringBoot development and webapp development.

**NOTE** This project makes use of Lombok, so make sure to install and activate a Lombok plugin, when working with the code.

 

