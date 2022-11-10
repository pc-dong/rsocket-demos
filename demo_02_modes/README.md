# RSocket client

[GitHub - rsocket/rsocket-cli: Command-line client for ReactiveSocket](https://github.com/rsocket/rsocket-cli)

[RSocket Client CLI (RSC)](https://github.com/making/rsc)

rsocket-cli --route=splitString --stream -i Sunday tcp://localhost:7001
rsc --route=splitString --stream --data=Sunday tcp://localhost:7001


rsocket-cli --route=log --fnf -i Sunday tcp://localhost:7001
rsc --route=log --fnf --data=Sunday --debug tcp://localhost:7001

rsocket-cli --route=toUpperCase --request -i Sunday tcp://localhost:7001
rsc --route=toUpperCase --request --data=Sunday --debug tcp://localhost:7001


rsocket-cli --route=channelToUpperCase --channel -i Sunday tcp://localhost:7001
rsc --route=channelToUpperCase --channel --data=- --debug tcp://localhost:7001
