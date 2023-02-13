<template>
  <div class="hello">
    <h1><input v-model="useMessage.msg" id="msg"/></h1>
    <h1>{{ useMessage.msg }}</h1>
    <button v-on:click="useMessage.reverseMessage">反转字符串</button>
    <h1>{{ useMessage.reverse }}</h1>
    <button @click="useMessage.toUpperCase">小写转大写</button>
    <h1>{{ useMessage.upper }}</h1>
    <button v-on:click="useMessage.split">Split</button>
    <div>
      <ol id="splits">
        <li v-bind:key="spl.index" v-for="spl in useMessage.splits">
          {{ spl.text }}
        </li>
      </ol>
    </div>
    <h1><button v-on:click="useMessage.log">log</button></h1>
    <h1><button v-on:click="useMessage.channelToUpperCase">channelToUpperCase</button></h1>
    <br/>
    <div>
      <ol id="channels">
        <li v-bind:key="cha.index" v-for="cha in useMessage.channels">
          {{ cha.text }}
        </li>
      </ol>
    </div>
  </div>
</template>

<script setup lang="ts">
import {useMessageStore} from "@/message";
const useMessage = useMessageStore()
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
