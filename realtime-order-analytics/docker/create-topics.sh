#!/usr/bin/env bash
set -e
BOOTSTRAP=localhost:9092
KAFKA_CONTAINER=$(docker ps --filter "ancestor=confluentinc/cp-kafka:7.4.1" -q | head -n1)
if [ -z "$KAFKA_CONTAINER" ]; then
  echo "Kafka container not found. Make sure docker-compose is up."
  exit 1
fi
docker exec -i "$KAFKA_CONTAINER" kafka-topics --create --topic orders --bootstrap-server $BOOTSTRAP --partitions 3 --replication-factor 1 || true
echo "Topic created (or already existed)."
