/**
 * This file is originally from:
 * https://github.com/tensorflow/tfjs-examples/blob/master/webcam-transfer-learning/webcam.js
 */

import * as tf from '@tensorflow/tfjs';
import yolo from "tfjs-yolo-tiny";

/**
 * A class that wraps image elements to capture Tensor4Ds.
 */
export class ImageAnalyzer {
    /**
     * @param {HTMLImageElement} webcamElement A HTMLImageElement to be analyzed.
     * @param {model} model The trained model to analyze against
     */
    constructor(imageElement, model) {
        this.imageElement = imageElement;
        this.model = model
    }

    /**
     * Analyze image and return boxes
     */
    async boxes() {
        console.log('Analyzing');
        const tensor = this.capture();
        const boxes = await yolo(tensor, this.model);
        tensor.dispose();
        return boxes;
    }

    /**
     * Captures a pixels from the element and normalizes it between -1 and 1.
     * Returns a batched image (1-element batch) of shape [1, w, h, c].
     */
    capture() {
        return tf.tidy(() => {
            // Reads the image as a Tensor from the img element.
            const imagePixels = tf.fromPixels(this.imageElement);

            // Crop the image so we're using the center square
            const croppedImage = this.cropImage(imagePixels);

            // Expand ^the outer most dimension so we have a batch size of 1.
            const batchedImage = croppedImage.expandDims(0);

            // Normalize the image between -1 and 1. The image comes in between 0-255,
            return batchedImage.toFloat().div(tf.scalar(255));
        });
    }

    /**
     * Crops an image tensor so we get a square image with no white space.
     * @param {Tensor4D} img An input image Tensor to crop.
     */
    cropImage(img) {
        const size = Math.min(img.shape[0], img.shape[1]);
        const centerHeight = img.shape[0] / 2;
        const beginHeight = centerHeight - (size / 2);
        const centerWidth = img.shape[1] / 2;
        const beginWidth = centerWidth - (size / 2);
        return img.slice([beginHeight, beginWidth, 0], [size, size, 3]);
    }

}
