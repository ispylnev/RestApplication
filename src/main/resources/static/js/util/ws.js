import SockJS from 'sockjs-client'
import {Stomp} from "@stomp/stompjs/esm5/compatibility/stomp";

var stompClient = null;
const handlers = [];

export function connect() {
    var socket = new SockJS('/gs-guide-websocket');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        stompClient.subscribe('/topic/activity', message => {
            handlers.forEach(handler => handler(JSON.parse(message.body)))
        })
    });
}

export function addHandler(handler) {
    handlers.push(handler)
}

export function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect()
    }
    console.log("Disconnected")
}

export function sendMessage(message) {
    connect();
    setTimeout(function () {
        stompClient.publish({destination: "/app/changeMessage", body: JSON.stringify(message)});
    }, 100);
}

