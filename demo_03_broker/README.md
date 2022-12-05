# RSocket client

[RSocket Client CLI (RSC)](https://github.com/making/rsc)

```shell
rsc --route=splitString --stream --data=Sunday tcp://localhost:7002
````


```shell
rsc --route=log --fnf --data=Sunday --debug tcp://localhost:7002

```

rsocket-cli --route=toUpperCase --request -i Sunday tcp://localhost:7002
```shell
rsc --route=toUpperCase --request -m 123 --mmt message/x.client.id --data=Sunday --debug tcp://localhost:7002

```

## backPressure
```shell
rsc --route=repeatToUpperCase --stream --data=Sunday tcp://localhost:7002
 --take 10
```
```shell
rsc --route=repeatToUpperCase --stream --delayElement=100 --data=Sunday tcp://localhost:7002
 --take 10
```
```shell
rsc --route=repeatToUpperCase --stream --limitRate=4  --data=Sunday tcp://localhost:7002
 --take 10
```

## session resumption

```shell
socat -d TCP-LISTEN:7003,fork,reuseaddr TCP:localhost:7002

```

```shell
rsc --route=repeatToUpperCase --stream --resume 60 --limitRate=1 --delayElement=1000 --data=Sunday tcp://localhost:7003
```

```shell
rsocket-cli --route=channelToUpperCase --channel -i Sunday tcp://localhost:7002

```

```shell
rsc --route=channelToUpperCase --channel --data=- --debug tcp://localhost:7002

```

## Posters CRUD

### Create
```shell
rsc --route=posters.post --request --data='{"title": "title1", "content": "poster content"}' --debug tcp://localhost:7002

```

### List all
```shell
rsc --route=posters.get --stream --debug tcp://localhost:7002

```

### Get by ID 
```shell
 rsc --route=posters.bd75d9d7-80ed-4f4d-98d4-57d2faf33300.get --request --debug tcp://localhost:7002
 
```

### Update
```shell

 rsc --route=posters.bd75d9d7-80ed-4f4d-98d4-57d2faf33300.put --request --data='{"title": "title2", "content": "poster content2"}' --debug tcp://localhost:7002
 
```


### Delete
```shell
 rsc --route=posters.44ce108c-942e-41f9-9b39-a590f16cce22.delete --request --debug tcp://localhost:7002
```