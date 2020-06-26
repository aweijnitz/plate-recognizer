'use strict';
var socketHost = 'http://localhost:8080'
//var socket = new SockJS(socketHost + '/websocketApp');
var websocketURL = "ws://localhost:8080/websocketApp"
var stompClient = null;
var name = null;
var subscriptionMessage = null;
var subscriptionUser = null;
var subscriptionErrors = null;

function connectionSuccess() {

    var messageContent = {clientId: name, messageId: "id123", imageData: "ConnectionSuccess-longString"} //document.querySelector('#chatMessage').value.trim();

    console.log('CONNECT OK!')
    //stompClient.subscribe('/topic/javainuse', onMessageReceived);
    subscriptionMessage = stompClient.subscribe('/message', onMessageReceived);
    subscriptionUser = stompClient.subscribe('/queue/reply', onMessageReceived);
    subscriptionErrors = stompClient.subscribe('/queue/errors', onMessageReceived);
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

function onMessageReceived(payload) {
    console.log('Message RECEIVED')
    //var message = JSON.parse(payload);
    //console.log(JSON.stringify(payload));
    console.log(JSON.parse(payload.body).message);

}


function connect(nameStr) {
    name = 'name:' + nameStr.trim();
    //stompClient = Stomp.over(socket);
    stompClient = webstomp.client(websocketURL, {
        protocols: ['v12.stomp'],
        binary: true,
        heartbeat: {incoming: 10000, outgoing: 10000},
        debug: false
    });
    stompClient.connect({}, connectionSuccess, connectionFailed);
    //stompClient.disconnect(connectionDisconnect, {})
}
