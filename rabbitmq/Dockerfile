FROM rabbitmq:3-management

RUN rabbitmq-plugins enable --offline rabbitmq_web_stomp  rabbitmq_auth_backend_http && \
    echo "auth_backends.1 = rabbit_auth_backend_http" >> /etc/rabbitmq/rabbitmq.conf && \
    echo "auth_backends.2 = internal" >> /etc/rabbitmq/rabbitmq.conf && \
    echo "auth_http.http_method = post" >> /etc/rabbitmq/rabbitmq.conf && \
    echo "auth_http.user_path = $AUTH_HOST/auth/amqp/user" >> /etc/rabbitmq/rabbitmq.conf && \
    echo "auth_http.vhost_path = $AUTH_HOST/auth/amqp/vhost" >> /etc/rabbitmq/rabbitmq.conf && \
    echo "auth_http.resource_path = $AUTH_HOST/auth/amqp/resource" >> /etc/rabbitmq/rabbitmq.conf && \
    echo "auth_http.topic_path = $AUTH_HOST/auth/amqp/topic" >> /etc/rabbitmq/rabbitmq.conf && \
    echo "loopback_users = none" >> /etc/rabbitmq/rabbitmq.conf
