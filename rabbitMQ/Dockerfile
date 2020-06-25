FROM rabbitmq:3-management
# Smaller image, but littel insight. Alpine version
# FROM rabbitmq:3.7-alpine

# RabbitMQ broker
EXPOSE 5672

# Rabbit MQ Manager (http://localhost:15672/)
EXPOSE 15672

# Stomp Protocol Plugin
EXPOSE 61613

# MQTT Protocol Plugin
EXPOSE 1883

# For more options, see https://hub.docker.com/_/rabbitmq
RUN rabbitmq-plugins enable --offline rabbitmq_mqtt rabbitmq_federation_management rabbitmq_stomp
