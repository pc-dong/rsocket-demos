import {RsocketRequester} from "@/services/rsocket-requester";
import {defineStore} from "pinia";
import {Flowable} from "rsocket-flowable";

const requester = new RsocketRequester()
export const useMessageStore = defineStore('message', {
    state: () => ({
        msg: "Welcome to Your Vue.js App!",
        reverse: "",
        upper: "",
        splits: [] as any[],
        channels: [] as any[],
    }),
    actions: {
        toUpperCase() {
            // this.upper = this.msg.toUpperCase()
            requester.route("toUpperCase")
                .data(this.msg)
                .retrieveSingle()
                .then(data => {
                    console.log("toUpperCase receive: " + data)
                    this.upper = JSON.stringify(data)
                })
        },
        reverseMessage() {
            requester.route("reverse")
                .data(this.msg)
                .retrieveSingle()
                .then(data => {
                    this.reverse = JSON.stringify(data)
                })
        },
        async split() {
            let index = 0;
            this.splits = []
            requester.route("split")
                .data(this.msg)
                .retrieveFlowable()
                .map(data => {
                    console.log("toUpperCase receive: " + data)
                    this.splits.push({index: index++, text: data})
                })
                .subscribe({
                    onComplete: () => console.log('done'),
                    onError: (error) => {
                        console.log(error)
                        this.splits.push({index: 0, text: error.message})
                    },
                    onNext: (value) => console.log(value),
                    // Nothing happens until `request(n)` is called
                    onSubscribe: (sub) => sub.request(100),
                })
        },
        async log() {
            requester.route("log")
                .data(this.msg)
                .retrieveVoid()
        },
        async channelToUpperCase() {
            let index = 0;
            this.channels = []
            requester.route("channelToUpperCase")
                .data(Flowable.just(this.msg + "1",
                    this.msg + "2", this.msg + "3", this.msg + "4", this.msg + "5"))
                .retrieveFlowable()
                .map(data => {
                    console.log("channel toUpperCase receive: " + data)
                    this.channels.push({index: index++, text: data})
                })
                .subscribe({
                    onComplete: () => console.log('done'),
                    onError: (error) => {
                        console.log(error)
                        this.channels.push({index: 0, text: error.message})
                    },
                    onNext: (value) => {
                        console.log(value)
                    },
                    // Nothing happens until `request(n)` is called
                    onSubscribe: (sub) => sub.request(100),
                })
        }
    }
})