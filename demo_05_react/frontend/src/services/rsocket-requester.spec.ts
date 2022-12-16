import {encodeCompositeMetadata, encodeRoute, WellKnownMimeType} from "rsocket-composite-metadata";
import MESSAGE_RSOCKET_ROUTING = WellKnownMimeType.MESSAGE_RSOCKET_ROUTING;
import type {Payload} from "rsocket-core";
import type {RsocketRequester} from "./rsocket-requester";
import {Flowable, Single} from "rsocket-flowable";


export class RsocketRequesterSpec {
    constructor(private readonly requester: RsocketRequester,
                private readonly route: string) {
    }

    private _data: any;
    private _metadata: Map<WellKnownMimeType, Buffer> = new Map<WellKnownMimeType, Buffer>();


    public data(data: any): RsocketRequesterSpec {
        this._data = data;
        return this;
    }

    public metadata(type: WellKnownMimeType, data: Buffer): RsocketRequesterSpec {
        this._metadata.set(type, data);
        return this;
    }

    public retrieveSingle(): Single<any> {
        const map = new Map<WellKnownMimeType, Buffer>();
        map.set(MESSAGE_RSOCKET_ROUTING, encodeRoute(this.route));
        this._metadata.forEach((value, key) => {
            map.set(key, value)
        })
        const metadata = encodeCompositeMetadata(map);
        const payload = {data: Buffer.from(this.isString(this._data) ? this._data : JSON.stringify(this._data), "utf-8"), metadata} as Payload
        return new Single((subscriber: any) =>  {
            this.requester.getRSocket()
                .then(rsocket => {
                    rsocket.requestResponse(payload, {
                        onComplete(): void {
                            subscriber.onComplete(null)
                        },
                        onError(error: Error): void {
                            subscriber.onError(error)
                        },
                        onExtension(extendedType: number, content: Buffer | null | undefined, canBeIgnored: boolean): void {
                        },
                        onNext(payload: Payload, isComplete: boolean) {
                            let result;
                            const data = payload?.data?.toString("utf-8") || "";
                            try { result =  eval(data)}
                            catch (err) {
                                result = data
                            }
                            subscriber.onComplete(result)
                        }
                    })
                    subscriber.onSubscribe()
                })
        })

    }

    private isString(data: any){
        return Object.prototype.toString.call(data) === '[object String]';
    }

    public retrieveFlowable(): Flowable<any> {
        const map = new Map<WellKnownMimeType, Buffer>();
        map.set(MESSAGE_RSOCKET_ROUTING, encodeRoute(this.route));
        this._metadata.forEach((value, key) => {
            map.set(key, value)
        })
        const metadata = encodeCompositeMetadata(map);
        if(this._data instanceof Flowable) {
            return this.retrieveChannelFlowable(metadata);
        } else {
            return this.retrieveStreamFlowable(metadata);
        }
    }

    private retrieveChannelFlowable(metadata: Buffer) {
        return new Flowable<any>((subscriber: any) => {
            let requester: any;
            this._data.subscribe({
                onComplete() {
                    subscriber.onComplete()
                },
                onError: (error: any) => {
                    subscriber.onError(error)
                },
                onNext: (value: any) => {
                    if (!requester) {
                        this.requester.getRSocket()
                            .then(rsocket => {
                                const payload = {
                                    data: Buffer.from(this.isString(value) ? value : JSON.stringify(value), "utf-8"),
                                    metadata
                                } as Payload
                                requester = rsocket.requestChannel(payload, 1, false, {
                                    cancel(): void {
                                        subscriber.onComplete()
                                    },
                                    request(requestN: number): void {
                                        requester.request(requestN)
                                    },
                                    onComplete(): void {
                                        subscriber.onComplete()
                                    },
                                    onError(error: Error): void {
                                        subscriber.onError(error)
                                    },
                                    onExtension(extendedType: number, content: Buffer | null | undefined, canBeIgnored: boolean): void {
                                    },
                                    onNext(payload: Payload, isComplete: boolean) {
                                        let result;
                                        const data = payload?.data?.toString("utf-8") || "";
                                        try {
                                            result = eval(data)
                                        } catch (err) {
                                            result = data
                                        }
                                        subscriber.onNext(result)
                                    }
                                })
                            })
                    } else {
                        const payload = {data: Buffer.from(this.isString(value) ? value : JSON.stringify(value), "utf-8")} as Payload
                        requester.onNext(payload, false)
                    }
                },
                // Nothing happens until `request(n)` is called
                onSubscribe: (sub: any) => {
                    subscriber.onSubscribe(sub)
                }
            })
        })
    }

    private retrieveStreamFlowable(metadata: Buffer) {
        const payload = {
            data: Buffer.from(this.isString(this._data) ? this._data : JSON.stringify(this._data), "utf-8"),
            metadata
        } as Payload
        return new Flowable((subscriber: any) => {
            this.requester.getRSocket()
                .then(rsocket => {
                    rsocket.requestStream(payload, 100, {
                        onComplete(): void {
                            subscriber.onComplete()
                        },
                        onError(error: Error): void {
                            subscriber.onError(error)
                        },
                        onExtension(extendedType: number, content: Buffer | null | undefined, canBeIgnored: boolean): void {
                        },
                        onNext(payload: Payload, isComplete: boolean) {
                            let result;
                            const data = payload?.data?.toString("utf-8") || "";
                            try {
                                result = eval(data)
                            } catch (err) {
                                result = data
                            }
                            subscriber.onNext(result)
                        }
                    })
                    subscriber.onSubscribe()
                })
        })
    }

    public retrieveVoid(): void {
        const map = new Map<WellKnownMimeType, Buffer>();
        map.set(MESSAGE_RSOCKET_ROUTING, encodeRoute(this.route));
        this._metadata.forEach((value, key) => {
            map.set(key, value)
        })
        const metadata = encodeCompositeMetadata(map);
        const payload = {data: Buffer.from(this.isString(this._data) ? this._data : JSON.stringify(this._data), "utf-8"), metadata} as Payload
            this.requester.getRSocket()
                .then(rsocket => {
                    rsocket.fireAndForget(payload,  {
                        onComplete(): void {
                        },
                        onError(error: Error): void {
                            throw error
                        }
                    })
                })
    }


}