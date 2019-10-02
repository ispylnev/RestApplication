import SockJS from 'sockjs-client'
import { Stomp } from '@stomp/stompjs'


var stompClient = null
const handlers = []

export function connect() {
    // const socket = new SockJS('/gs-guide-websocket')
    stompClient = Stomp.over(function () {
        return new SockJS('/gs-guide-websocket')
    })
    // stompClient = Stomp.client('ws://localhost::15674/ws')
    // stompClient.connect({}, frame => {
    //     console.log("handllers:" + handlers)
    //
    //     console.log('Connected: ' + frame)
    //     stompClient.subscribe('/topic/activity', message => {
    //         handlers.forEach(handler => handler(JSON.parse(message.body)))
    //     })
    // })
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
    connect()
    console.log("send message: " + message);
    console.log("using client: " + stompClient);
    stompClient.send("/app/changeMessage", {}, JSON.stringify(message))
}