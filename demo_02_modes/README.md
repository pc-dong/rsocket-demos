# RSocket client

[GitHub - rsocket/rsocket-cli: Command-line client for ReactiveSocket](https://github.com/rsocket/rsocket-cli)

[RSocket Client CLI (RSC)](https://github.com/making/rsc)

```shell
rsocket-cli --route=splitString --stream -i Sunday tcp://localhost:7001
```

```shell
rsc --route=splitString --stream --data=Sunday tcp://localhost:7001
````

```shell
rsocket-cli --route=log --fnf -i Sunday tcp://localhost:7001
```

```shell
rsc --route=log --fnf --data=Sunday --debug tcp://localhost:7001
```

rsocket-cli --route=toUpperCase --request -i Sunday tcp://localhost:7001
```shell
rsc --route=toUpperCase --request -m 123 --mmt message/x.client.id --data=Sunday --debug tcp://localhost:7001
```

## backPressure
```shell
rsc --route=repeatToUpperCase --stream --data=Sunday tcp://localhost:7001 --take 10
```
```shell
rsc --route=repeatToUpperCase --stream --delayElement=100 --data=Sunday tcp://localhost:7001 --take 10
```
```shell
rsc --route=repeatToUpperCase --stream --limitRate=4  --data=Sunday tcp://localhost:7001 --take 10
```

## session resumption

```shell
socat -d TCP-LISTEN:7002,fork,reuseaddr TCP:localhost:7001
```

```shell
rsc --route=repeatToUpperCase --stream --resume 60 --limitRate=1 --delayElement=1000 --data=Sunday tcp://localhost:7002
```

```shell
rsocket-cli --route=channelToUpperCase --channel -i Sunday tcp://localhost:7001
```

```shell
rsc --route=channelToUpperCase --channel --data=- --debug tcp://localhost:7001
```
