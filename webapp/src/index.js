import {downloadModel} from "tfjs-yolo-tiny";
import {ImgSubmitter} from "./lib/ImgSubmitter";
import {ImageAnalyzer} from "./lib/ImageAnalyzer";
import {MessageQueue} from "./lib/MessageQueue";
import {model} from "@tensorflow/tfjs/.yalc/@tensorflow/tfjs-layers";

$(async () => {
    console.log('Initializing');

    const cameraConfig = {
        format: 'image/jpeg'
    };

    const communicationConfig = {
        clientId: "Testclient",
        imageSubmitEndpoint: "http://localhost:8080/api/v1/submit",
        websocketURL: "ws://localhost:8080/websocket",
    };
    const messageQueue = new MessageQueue(communicationConfig);
    const submitter = new ImgSubmitter(communicationConfig.imageSubmitEndpoint, communicationConfig.clientId);
    let width = 800;
    let height = null;
    let targetWidth = 416;     // Target dimensions. The model is trained on this dimension.
    let targetHeight = 416;
    let streaming = false;
    let videoElement = null;
    let canvasElement = null; // Hidden canvas as workarea
    let photoElement = null; // The picture snapped from the video stream
    let recognizedPlatesElement = null;
    let takePicButton = null;
    let detectorModel = null;
    let doImageAnalysis = true;

    // ====== UTILITIES
    // ----------------

    const clearPhoto = () => {

        let context = canvasElement.getContext('2d');
        context.fillStyle = "#AAA";
        context.fillRect(0, 0, canvasElement.width, canvasElement.height);
        let data = canvasElement.toDataURL('image/png');
        photoElement.setAttribute('src', data);
    };

    /**
     * Adjusts the size so we can make a centered square crop without
     * including whitespace.
     * @param {number} width The real width of the element element.
     * @param {number} height The real height of the element element.
     */
    const adjustElementSize = (element, width, height) => {
        const aspectRatio = width / height;
        if (width >= height) {
            element.width = aspectRatio * element.height;
        } else if (width < height) {
            element.height = element.width / aspectRatio;
        }
        return {width: element.width, height: element.height};
    }

    const clearRects = () => {
        const rects = document.getElementsByClassName('rect');
        while (rects[0]) {
            rects[0].parentNode.removeChild(rects[0]);
        }
    };

    const isWorthSubmitting = box => {
        const {
            top, left, bottom, right, classProb, className,
        } = box;

        let isPlateVehicle = (className === 'car' || className === 'truck');
        let isLargeEnough = (right - left > 128);
        let probabilityThreshold = 0.4;
        return true; // (isPlateVehicle && isLargeEnough && classProb > probabilityThreshold)
    };

    function grabPixels(box) {
        console.log("Grab", box);
        let width = box.right - box.left;
        let height = box.bottom - box.top;
        let context = canvasElement.getContext("2d");
        context.clearRect(0, 0, targetWidth, targetHeight);
        context.drawImage(photoElement, box.left, box.top, width, height, 0, 0, targetWidth, targetHeight);
        return canvasElement.toDataURL(cameraConfig.format);
    }


    const drawRect = (box, color = 'red') => {
        if (!isWorthSubmitting(box)) // Skip non-vehicles and things that are too small to use
            return;

        const parent = document.getElementById('photoWrapperId');
        // Recalculate coordinates
        let left = Math.round(box.left + photoElement.offsetLeft);
        let top = Math.round(box.top + photoElement.offsetTop);
        let boxWidth = Math.round(box.right - box.left);
        let boxHeight = Math.round(box.bottom - box.top);
        const rect = document.createElement('div');
        rect.classList.add('rect');
        const posText = `top: ${top}px; left: ${left}px; width: ${boxWidth}px; height: ${boxHeight}px; border-color: ${color}`;
        console.log(posText);
        rect.style.cssText = posText;

        rect.addEventListener('click', event => {
            let imageDataStr = grabPixels(box);
            submitter.submitImageData(imageDataStr, () => {
                console.log('submitted');
            });
            event.preventDefault();
        }, {once: true});

        document.getElementById("photoWrapperId").appendChild(rect);
    }

    const loadImage = async (element, imageUrl) => {
        const imageLoadPromise = new Promise(resolve => {
            element.onload = resolve;
            element.src = imageUrl;
        });

        await imageLoadPromise;
        console.log("image loaded");
        return element;
    }

    // ====== MAIN LOGIC
    // -------------------------


    const handleAnalysisResult = boxes => {
        console.log(boxes);
        clearRects();
        boxes.forEach(box => {
            const {
                className,
            } = box;

            let isPlateVehicle = (className === 'car' || className === 'truck');
            drawRect(box, (isPlateVehicle ? 'green' : 'red'));
        });
    };

    const takePicture = async () => {
        console.log('Taking pic');
        let context = canvasElement.getContext('2d');
        canvasElement.width = targetWidth; //width;
        canvasElement.height = targetHeight; //height;
        context.drawImage(videoElement, 0, 0, targetWidth, targetHeight);

        let data = canvasElement.toDataURL(cameraConfig.format);
        $(videoElement).toggle();
        $(photoElement).toggle();
        photoElement.width = targetWidth;
        photoElement.height = targetHeight;
        photoElement = await loadImage(photoElement, data);
        let analyzer = new ImageAnalyzer(photoElement, detectorModel);
        const boxes = await analyzer.boxes()
        handleAnalysisResult(boxes);
    }

    const onPlateMessageHandler = msg => {
        console.log("Msg Handler: ", msg);
        let body = JSON.parse(msg.body);
        let regions = body.regions;
        if(regions.length === 0)
            return;
        let div = document.createElement("div");
        let regionsText = '';
        regions.forEach((el, i) => i > 0 ? regionsText += ', ' + el : regionsText += el);
        let text = document.createTextNode(regionsText);
        div.appendChild(text);
        recognizedPlatesElement.append(div);
    }

    // ====== APP INITIALIZATION
    // -------------------------

    const initYOLO = async () => {
        try {
            detectorModel = await downloadModel();
            //alert("We'll ask to access your webcam so that we can detect objects.");
            //doneLoading();
        } catch (e) {
            console.error(e);
            //alert("Failed to download machine learning model.");
        }
    };


    const init = async () => {
        videoElement = document.getElementById('videoId');
        canvasElement = document.getElementById('photoCanvasId');
        photoElement = document.getElementById('photoId');
        recognizedPlatesElement = document.getElementById('recognizedPlatesId');
        $(photoElement).hide();
        takePicButton = document.getElementById('takePhotoButtonId');
        videoElement.setAttribute('width', width);
        canvasElement.setAttribute('width', width);

        // Connect and start listening for plate messages
        messageQueue.connect(onPlateMessageHandler);
        await initYOLO();
        navigator.mediaDevices.getUserMedia({
            'audio': false,
            'video': {
                facingMode: 'environment',
                width: {ideal: targetWidth},
                height: {ideal: targetHeight}
            }
        }).then((stream) => {
            let videoSettings = stream.getVideoTracks().pop().getSettings();
            console.log('REAL VIDEO: ' + videoSettings.width + ' x ' + videoSettings.height);
            videoElement.srcObject = stream;
            videoElement.play();
            const newDim = adjustElementSize(videoElement)//, videoSettings.width, videoSettings.height);
            width = newDim.width;
            height = newDim.height;
            console.log('videoWidth: ' + videoElement.width + 'videoHeight: ' + videoElement.height);
        }).catch(err => {
            alert("Failed to start camera");
            console.log("An error occurred: " + err);
        });

        const takePhotoClickHandler = (ev) => {
            if (doImageAnalysis)
                takePicture();
            else {
                $(videoElement).toggle();
                $(photoElement).toggle();
            }
            doImageAnalysis = !doImageAnalysis; // The button does double duty as "close photo", so we keep track of the state
            ev.preventDefault();
        };

        videoElement.addEventListener('canplay', (ev) => {
            if (!streaming) {
                height = videoElement.videoHeight / (videoElement.videoWidth / width);
                streaming = true;
            }
        }, false);

        videoElement.addEventListener('click', takePhotoClickHandler, false);
        takePicButton.addEventListener('click', takePhotoClickHandler, false);

        clearPhoto();
    }; // end init


    init();
});