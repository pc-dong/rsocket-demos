import React from "react";
import {RsocketRequester} from "../services/rsocket-requester";
import {Flowable} from "rsocket-flowable";

export class RSocketComponent extends React.Component<any, any> {
    private requester;
    constructor(props: any) {
        super(props);
        this.state = {message: "hello Rsocket", upperMsg: "", count: 1,
            splits: [],
            channels: []
        };
        this.requester = new RsocketRequester();
    }



    render() {
        const toUpperCase =  async () => {
            this.setState({upperMsg: ""});
            this.requester.route("toUpperCase")
                .data(this.state.message)
                .retrieveSingle()
                .then(data => {
                    console.log("toUpperCase receive: " + data)
                    this.setState({upperMsg: this.state.message.toUpperCase(), count: this.state.count + 1});
                })
            return undefined
        }

        const log =  async () => {
            this.requester.route("log")
                .data(this.state.message)
                .retrieveVoid()
            return undefined
        }

        const channelToUpperCase = async ()=> {
            let index = 0;
            this.setState({channels: []})
            this.requester.route("channelToUpperCase")
                .data(Flowable.just(this.state.message + "1",
                    this.state.message + "2", this.state.message + "3", this.state.message + "4", this.state.message + "5"))
                .retrieveFlowable()
                .map(data => {
                    console.log("channel toUpperCase receive: " + data)
                    const channels = this.state.channels
                    channels.push({index: index++, text: data})
                    this.setState({channels})                })
                .subscribe({
                    onComplete: () => console.log('done'),
                    onError: (error) => {
                        console.log(error)
                        const channels = this.state.channels
                        channels.push({index: 0, text: error.message})
                        this.setState({channels})
                    },
                    onNext: (value) => {console.log(value)},
                    // Nothing happens until `request(n)` is called
                    onSubscribe: (sub) => sub.request(100),
                })
        }

        const split =  async () => {
            let index = 0;
            this.setState({splits: []})
            this.requester.route("split")
                .data(this.state.message)
                .retrieveFlowable()
                .map(data => {
                    console.log("toUpperCase receive: " + data)
                    const splits = this.state.splits
                    splits.push({index: index++, text: data})
                    this.setState({splits})
                })
                .subscribe({
                    onComplete: () => console.log('done'),
                    onError: (error) => {
                        console.log(error)
                        const splits = this.state.splits
                        splits.push({index: 0, text: error.message})
                        this.setState({splits})
                    },
                    onNext: (value) => console.log(value),
                    // Nothing happens until `request(n)` is called
                    onSubscribe: (sub) => sub.request(100),
                })
            return undefined
        }

        const listItems = this.state.splits.map((item: any) =>
            <li key = {item.index}>{item.text}</li>
        );

        const listChannels = this.state.channels.map((item: any) =>
            <li key = {item.index}>{item.text}</li>
        );

        return (
            <div>
                <h1>{this.state.message}</h1>
                <h2> {this.state.count}</h2>
                <h1><button onClick={log}>log</button></h1>
                <h1><button onClick={toUpperCase}>to Uppercase</button></h1>
                <h2> {this.state.upperMsg}</h2>
                <h1><button onClick={split}>Split</button></h1>
                <h2>
                    <ul>
                        {listItems}
                </ul></h2>
                <h1><button onClick={channelToUpperCase}>channelToUpperCase</button></h1>
                <h2>
                    <ul>
                        {listChannels}
                    </ul></h2>
            </div>
        );
    }
}