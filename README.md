# SpringBoot-Redis-Lettuce-Cluster-Pool-Example



### dependencies

	implementation 'org.springframework.boot:spring-boot-starter-data-redis'
	implementation 'io.lettuce:lettuce-core'
	implementation 'org.apache.commons:commons-pool2:2.11.1'

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
      - '8003:8003'
      - '8004:8004'
      - '8005:8005'
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

  redis-node-3:
    image: redis:alpine
    command: redis-server /usr/local/etc/redis/redis.conf
    network_mode: "service:redis-node-0"
    volumes:
      - ./redis-cluster/8003/redis.conf:/usr/local/etc/redis/redis.conf

  redis-node-4:
    image: redis:alpine
    command: redis-server /usr/local/etc/redis/redis.conf
    network_mode: "service:redis-node-0"
    volumes:
      - ./redis-cluster/8004/redis.conf:/usr/local/etc/redis/redis.conf

  redis-node-5:
    image: redis:alpine
    command: redis-server /usr/local/etc/redis/redis.conf
    network_mode: "service:redis-node-0"
    volumes:
      - ./redis-cluster/8005/redis.conf:/usr/local/etc/redis/redis.conf

```

### application.yaml

````yaml

spring:
  #  autoconfigure.exclude:
  #    - org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration
  #    - org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration
  redis:
    cluster:
      nodes:
        - 0.0.0.0:8000
        - 0.0.0.0:8001
        - 0.0.0.0:8002
        - 0.0.0.0:8003
        - 0.0.0.0:8004
        - 0.0.0.0:8005
    #      max-redirects: 3
    password: myredis
    lettuce:
      pool:
        enabled: true
        max-idle: 16
        max-active: 32
        min-idle: 8

````

### noted

```java

    private static final boolean COMMONS_POOL2_AVAILABLE = ClassUtils.isPresent("org.apache.commons.pool2.ObjectPool",
			RedisConnectionConfiguration.class.getClassLoader());

    protected boolean isPoolEnabled(Pool pool) {
        Boolean enabled = pool.getEnabled();
        return (enabled != null) ? enabled : COMMONS_POOL2_AVAILABLE;
    }

```

    - cluster supports for redis v.3 or greater.
    - pool enabled automatically if "commons-pool2" is available.

### Reference

- https://alex.dzyoba.com/blog/redis-cluster/
- https://redis.io/docs/manual/scaling/

