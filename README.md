# SpringBoot-Redis-Lettuce-Cluster-Example

### run redis cluster with docker 

    # run docker-compose
    docker compose -f docker-compose-redis-cluster.yaml up -d

    # exec contrainer    
    docker exec -it redis-cluster sh
    
    # run redis-cli commands for crate the cluster
    echo "yes" > temp
    redis-cli --cluster create 127.0.0.1:8000  127.0.0.1:8001  127.0.0.1:8002 127.0.0.1:8003 127.0.0.1:8004 127.0.0.1:8005 -a myredis --cluster-replicas 1 < temp

    # check cluster nodes
    redis-cli -c -p 8000 -a myredis cluster nodes


### docker-compose.yaml

```yaml

version: '3'
services:

  redis-node-0:
    image: redis:alpine
    container_name: redis-cluster
    ports:
      - '8000:8000'
      - '8001:8001'
      - '8002:8002'
    command: redis-server /usr/local/etc/redis/redis.conf
    volumes:
      - ./redis-cluster/8000/redis.conf:/usr/local/etc/redis/redis.conf

  redis-node-1:
    image: redis:alpine
    command: redis-server /usr/local/etc/redis/redis.conf
    network_mode: "service:redis-node-0"
    volumes:
      - ./redis-cluster/8001/redis.conf:/usr/local/etc/redis/redis.conf

  redis-node-2:
    image: redis:alpine
    command: redis-server /usr/local/etc/redis/redis.conf
    network_mode: "service:redis-node-0"
    volumes:
      - ./redis-cluster/8002/redis.conf:/usr/local/etc/redis/redis.conf

```

### application.yaml

````yaml

spring:
  redis:
    cluster:
      nodes:
        - 0.0.0.0:8000
        - 0.0.0.0:8001
        - 0.0.0.0:8002
      max-redirects: 3
    password: myredis
    lettuce:
      pool:
        max-idle: 16
        max-active: 32
        min-idle: 8

````

### Reference

- https://alex.dzyoba.com/blog/redis-cluster/
- https://redis.io/docs/manual/scaling/

