# License Plate Scanner Webpp

The webapp uses YOLO deep learning model to detect objects locally in your browser before uploading to the server. Detected cars and trucks are cropped to the bounding box and submitted to the server.

It saves bandwith and increases speed and accuracy in the license plate recognition step (server side).

Object detection done using [tfjs-yolo-tiny](https://github.com/ModelDepot/tfjs-yolo-tiny).

## Some dependencies only work with OLD node.js, so...

    nvm install v8
    ./installTools.sh

    
#### NOTE: node >= 10 (might work sometimes)

    yarn --ignore-engines 

## Install Dependencies

    ./installLocalDependencies.sh

## Serve Demo

    yarn serve

# Build

    yarn build
    
Built artifacts are saved to the `dist` folder.
