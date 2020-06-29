# License Plate Scanner Webpp

The webapp uses YOLO deep learning model to detect objects locally in your browser before uploading to the server. Detected cars and trucks are cropped to the bounding box and submitted to the server.

It saves bandwith and increases speed and accuracy in the license plate recognition step (server side).

Object detection done using [tfjs-yolo-tiny](https://github.com/ModelDepot/tfjs-yolo-tiny).

## Install Dependencies
    yarn
    
### NOTE: node >= 10
    yarn --ignore-engines 

## Serve Demo
    yarn serve

# Build
    yarn build
    
Built artifacts are save to the `dist` folder.
