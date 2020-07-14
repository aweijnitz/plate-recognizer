/**
 * MessageQueue handles messages from the server, containing found license plates.
 */
export class MessageQueue {

    constructor(queueConfiguration) {
        this.queueConfiguration = queueConfiguration;
    }

    connectionSuccess(plateMsgHandler, callback) {
        console.log('CONNECT OK!')
        this.isConnected = true;
        this.subscriptionParsedPlates =
            this.stompClient.subscribe('/queue/parsedPlatesQueue', plateMsgHandler);
        this.subscriptionUser = this.stompClient.subscribe('/topic/plates', plateMsgHandler);
        if(callback) callback(this.stompClient);
    }

    connectionFailed(error) {
        // display the error's message header:
        this.isConnected = false;
        console.log('CONNECTION FAILED');
        console.log(error);
        console.log(error.headers.message);
    };

    connectionDisconnect(arg) {
        this.isConnected = false;
        console.log('DISCONNECT');
        console.log(arg);
    };


    connect(plateMessageHandler, onConnectHandler) {
        this.stompClient = webstomp.client(this.queueConfiguration.websocketURL, {
            protocols: ['v12.stomp'],
            binary: true,
            heartbeat: {incoming: 10000, outgoing: 10000},
            debug: false
        });
        this.stompClient.connect({}, () => {
            this.connectionSuccess(plateMessageHandler, onConnectHandler);
        }, this.connectionFailed);
    }
}