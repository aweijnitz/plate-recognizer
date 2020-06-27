'use strict';
//var socketHost = 'http://localhost:8080'
//var socket = new SockJS(socketHost + '/websocketApp');
var websocketURL = config.websocketURL; //"ws://localhost:8080/websocket"
var stompClient = null; // See https://github.com/JSteunou/webstomp-client
var name = null;
var subscriptionMessage = null;
var subscriptionUser = null;
var subscriptionErrors = null;

function connectionSuccess(callback) {

    var messageContent = {clientId: name, messageId: "id123", imageData: "ConnectionSuccess-longString"} //document.querySelector('#chatMessage').value.trim();

    console.log('CONNECT OK!')
    //stompClient.subscribe('/topic/javainuse', onMessageReceived);
    subscriptionMessage = stompClient.subscribe('/message', onMessageReceived);
    //subscriptionMessage = stompClient.subscribe('/queue/parsedPlatesQueue', onMessageReceived);

    subscriptionUser = stompClient.subscribe('/topic/reply', onMessageReceived);
    subscriptionErrors = stompClient.subscribe('/topic/errors', onMessageReceived);
    callback(stompClient);
}

function connectionFailed(error) {
    // display the error's message header:
    console.log('CONNECTION FAILED');
    console.log(error);
    console.log(error.headers.message);
};

function connectionDisconnect(arg) {
    // display the error's message header:
    console.log('DISCONNECT');
    console.log(arg);
};

function sendMessage(str) {
    var messageContent = {
        clientId: name,
        messageId: "id123",
        imageData: "" + str
    };

    stompClient.send("/app/message",
        JSON.stringify(messageContent),
        {type: 'info.andersw.model.ImageMessage'});
}

function sendImageMessage(imageDataBase64) {
    var messageContent = {
        clientId: name,
        messageId: "id123",
        imageData: imageDataBase64
    };

    stompClient.send("/queue/images",
        JSON.stringify(messageContent),
        {type: 'info.andersw.model.ImageMessage'});
}

function onMessageReceived(payload) {
    console.log('Message RECEIVED')
    //var message = JSON.parse(payload);
    //console.log(JSON.stringify(payload));
    console.log(JSON.parse(payload.body).message);

}


function connect(nameStr, callback) {
    //name = 'name:' + nameStr.trim();
    config.clientId = nameStr;
    //stompClient = Stomp.over(socket);
    stompClient = webstomp.client(websocketURL, {
        protocols: ['v12.stomp'],
        binary: true,
        heartbeat: {incoming: 10000, outgoing: 10000},
        debug: false
    });
    stompClient.connect({}, () => { connectionSuccess(callback); }, connectionFailed);
    //stompClient.disconnect(connectionDisconnect, {})
}


