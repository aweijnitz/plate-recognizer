export class ImgSubmitter {
    /**
     * @param {HTMLVideoElement} webcamElement A HTMLVideoElement representing the webcam feed.
     */
    constructor(endpoint, clientId) {
        this.endpoint = endpoint;
        this.clientId = clientId;
        this.nextMessageIdCounter = 0;
    }


    createMessage(clientId, imageDataBase64) {
        let messageId = clientId + "." + this.nextMessageIdCounter++;
        return JSON.stringify({
            clientId: ""+clientId,
            messageId: messageId,
            imageData: imageDataBase64
        });
    }

    send(message, callback) {
        $.ajax({
            type: "POST",
            url: this.endpoint,
            dataType: 'json',
            contentType: 'application/json',
            data: message,
            success: callback
        });
    }

    submitImageData(imageDataStr, callback) {
        let msg = this.createMessage(this.clientId, imageDataStr);
        this.send(msg, callback);
    }

    submitImage(imgElement, callback) {
        alert("ImgSubmitter:submitImage Not yet implemented")
        let msg = this.createMessage(this.clientId, imgElementToBase64(imgElement)); // TODO: Add local helper imgElementToBase64
        this.send(msg, callback);
    }



}
