# step-0
使用成熟的第三方工具，如rpc使用brpc-java来实现，数据存储用rocksdb，
先实现raft的基本功能，然后再逐步替换核心组件

# step-1
实现raft的成员变更

# step-2
完善dhrpc,并且将其应用在dhraft中

# step-3
自己实现数据存储引擎dhdb，用于替换rocksdb