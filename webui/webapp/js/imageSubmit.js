
var nextMessageIdCounter = 0;

function createMessage(clientId, imageDataBase64) {
    var messageId = clientId + "." + nextMessageIdCounter++;
    return JSON.stringify({
        clientId: ""+clientId,
        messageId: messageId,
        imageData: imageDataBase64
    });
}

function submitImage(imgElement) {
    var msg = createMessage(config.clientId, imgElementToBase64(imgElement));
    $.ajax({
        type: "POST",
        //the url where you want to sent the userName and password to
        url: config.imageSubmitEndpoint,
        dataType: 'json',
        contentType: 'application/json',
        data: msg,
        success: function (data) {
            console.log("POSTED! Response ", data);
        }
    });
}

