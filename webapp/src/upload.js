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
    let cropPhotoCanvas = null; // Hidden canvas as workarea
    let recognizedPlatesElement = null;
    let uploadPic = null;
    let detectorModel = null;
    let doImageAnalysis = true;
    const photoElement = document.getElementById('uploadImage'); // this is where we display the dropped image
    photoElement.addEventListener('load', function() {
        analyzeDroppedImage(photoElement);
    });
    const dropTrayElement = document.getElementById('dropTray');
    dropTrayElement.addEventListener('drop', handleOnDropEvent, false);
    dropTrayElement.addEventListener('dragover',function(ev){
        ev.preventDefault();
    });
    dropTrayElement.addEventListener('ondrop',function(ev){
        ev.preventDefault();
    });

    // ====== UTILITIES
    // ----------------

    function handleOnDropEvent(e) {
        e.preventDefault();
        e.stopPropagation();
        clearRects();
        clearImage();
        clearPhotoCrop();


        let reader = new FileReader();
        reader.onload = function (event) {
            photoElement.src=event.target.result;
            console.log('Image in browser');
            $('#uploadImage').show();
        }
        let files = e.dataTransfer.files;
        reader.readAsDataURL(files[0]); // We just care about the first file today. No multi-file support
    }

    const clearImage = () => {
        photoElement.src = '';
    };

    const clearRects = () => {
        const rects = document.getElementsByClassName('rect');
        while (rects[0]) {
            rects[0].parentNode.removeChild(rects[0]);
        }
    };

    const clearPhotoCrop = () => {
        let context = cropPhotoCanvas.getContext('2d');
        context.fillStyle = "#AAA";
        context.fillRect(0, 0, cropPhotoCanvas.width, cropPhotoCanvas.height);
    };

    const isWorthSubmitting = box => {
        const {
            top, left, bottom, right, classProb, className,
        } = box;

        let isPlateVehicle = (className === 'car' || className === 'truck');
        let isLargeEnough = (right - left > 128);
        let probabilityThreshold = 0.4;
        return isPlateVehicle && isLargeEnough && classProb > probabilityThreshold;
    };

    function grabPixels(box) {
        console.log("Grab", box);
        clearPhotoCrop();
        let width = box.right - box.left;
        let height = box.bottom - box.top;
        let context = cropPhotoCanvas.getContext("2d");
        cropPhotoCanvas.width = width;
        cropPhotoCanvas.height = height;
        context.drawImage(photoElement, box.calculatedLeft, box.calculatedTop, width, height, 0, 0, width, height);
        return cropPhotoCanvas.toDataURL(cameraConfig.format);
    }


    const drawRect = (box, color = 'red') => {
        const showAllBoxes = true;
        if (!isWorthSubmitting(box) && !showAllBoxes) // Skip non-vehicles and things that are too small to use
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
        rect.style.cssText = posText;
        box.calculatedLeft = left;
        box.calculatedTop = top;

        rect.addEventListener('click', event => {
            let imageDataStr = grabPixels(box);
            submitter.submitImageData(imageDataStr, () => {
                console.log('submitted');
            });
            event.preventDefault();
        }, {once: false});

        document.getElementById("photoWrapperId").appendChild(rect);
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

    const analyzeDroppedImage = async (imgEl) => {
        console.log('Analyzing pic');
        let analyzer = new ImageAnalyzer(imgEl, detectorModel);
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
            //doneLoading();
        } catch (e) {
            console.error(e);
            alert("Failed to download machine learning model.");
        }
    };


    const init = async () => {
        $('#uploadImage').hide();
        recognizedPlatesElement = document.getElementById('recognizedPlatesId');
        uploadPic = document.getElementById('uploadPhotoButtonId');
        cropPhotoCanvas = document.getElementById('cropPhotoCanvasId');
        cropPhotoCanvas.width = targetWidth;
        cropPhotoCanvas.height = targetHeight;
        clearPhotoCrop();

        messageQueue.connect(onPlateMessageHandler);
        await initYOLO();
        const uploadWholeImageButtonClickHandler = (ev) => {
            ev.preventDefault();
            submitter.submitImageData(photoElement.src, () => {
                console.log('Whole image submitted');
            })
        };

        uploadPic.addEventListener('click', uploadWholeImageButtonClickHandler, false);

    }; // end init
    init();
});