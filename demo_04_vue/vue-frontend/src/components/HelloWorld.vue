<template>
  <div class="hello">
    <h1><input v-model="msg" id="msg"/></h1>
    <h1>{{ msg }}</h1>
    <button v-on:click="reverseMessage">反转字符串</button>
    <h1>{{ reverse }}</h1>
    <button v-on:click="toUpperCase">小写转大写</button>
    <h1>{{ upper }}</h1>
    <button v-on:click="split">Split</button>
    <div>
      <ol id="splits">
        <li v-bind:key="spl.index" v-for="spl in splits">
          {{ spl.text }}
        </li>
      </ol>
    </div>
    <h1><button v-on:click="log">log</button></h1>
    <h1><button v-on:click="channelToUpperCase">channelToUpperCase</button></h1>
    <br/>
    <div>
      <ol id="channels">
        <li v-bind:key="cha.index" v-for="cha in channels">
          {{ cha.text }}
        </li>
      </ol>
    </div>
  </div>
</template>

<script lang="ts">
import {RsocketRequester} from "@/services/rsocket-requester";
import {defineComponent} from "vue";
import {Flowable} from "rsocket-flowable";

export default defineComponent({
  data() {
    return {
      msg: "Welcome to Your Vue.js App!",
      reverse: "",
      upper: "",
      splits: [] as any[],
      channels: [] as any[],
      requester: new RsocketRequester()
    }
  },
  methods: {
    reverseMessage() {
      this.requester.route("reverse")
          .data(this.msg)
          .retrieveSingle()
          .then(data => {
            this.reverse = JSON.stringify(data)
          })
    },
    async toUpperCase() {
      this.requester.route("toUpperCase")
          .data(this.msg)
          .retrieveSingle()
          .then(data => {
            console.log("toUpperCase receive: " + data)
            this.upper = JSON.stringify(data)
          })
    },
    async split() {
      let index = 0;
      this.splits = []
      this.requester.route("split")
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
      this.requester.route("log")
          .data(this.msg)
          .retrieveVoid()
    },
    async channelToUpperCase() {
      let index = 0;
      this.channels = []
      this.requester.route("channelToUpperCase")
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
            onNext: (value) => {console.log(value)},
            // Nothing happens until `request(n)` is called
            onSubscribe: (sub) => sub.request(100),
          })
    }
  }
})
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
h3 {
  margin: 40px 0 0;
}

ul {
  list-style-type: none;
  padding: 0;
}

li {
  display: inline-block;
  margin: 0 10px;
}

a {
  color: #42b983;
}
</style>
