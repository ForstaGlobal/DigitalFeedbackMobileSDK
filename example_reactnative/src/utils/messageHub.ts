import EventEmitter from 'react-native/Libraries/vendor/emitter/EventEmitter';

const eventEmitter = new EventEmitter();

export default class MessageHub {
    private static _listeners = new Map();

    public static register(registeredObj: any, eventKey: string, handler: any) {
        const listener = eventEmitter.addListener(eventKey, handler);

        if (!MessageHub._listeners.has(registeredObj)) {
            MessageHub._listeners.set(registeredObj, []);
        }

        MessageHub._listeners.get(registeredObj).push(listener);
    }

    public static unregisterAll(registeredObj: any) {
        if (MessageHub._listeners.has(registeredObj)) {
            const list = MessageHub._listeners.get(registeredObj);
            for (const listener of list) {
                listener.remove();
            }

            MessageHub._listeners.delete(registeredObj);
        }
    }

    public static send(eventKey: string, payload: any = null) {
        eventEmitter.emit(eventKey, payload);
    }
}
