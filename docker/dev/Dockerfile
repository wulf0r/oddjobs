FROM eclipse-temurin:21-jdk-jammy

ARG GRADLE_VERSION=9.5.0
ARG NODE_VERSION=24.18.0
ARG PNPM_VERSION=11.8.0

ENV GRADLE_HOME=/opt/gradle \
    NODE_HOME=/opt/node \
    PATH=/opt/gradle/bin:/opt/node/bin:$PATH

RUN apt-get update \
    && apt-get install -y --no-install-recommends \
        ca-certificates \
        curl \
        git \
        postgresql-client \
        python3 \
        unzip \
        xz-utils \
    && rm -rf /var/lib/apt/lists/*

RUN curl -fsSLo /tmp/gradle.zip "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" \
    && unzip -q /tmp/gradle.zip -d /opt \
    && ln -s "/opt/gradle-${GRADLE_VERSION}" "${GRADLE_HOME}" \
    && rm /tmp/gradle.zip \
    && gradle --version

RUN curl -fsSLo /tmp/node.tar.xz "https://nodejs.org/dist/v${NODE_VERSION}/node-v${NODE_VERSION}-linux-x64.tar.xz" \
    && mkdir -p "${NODE_HOME}" \
    && tar -xJf /tmp/node.tar.xz -C "${NODE_HOME}" --strip-components=1 \
    && rm /tmp/node.tar.xz \
    && corepack enable \
    && corepack prepare "pnpm@${PNPM_VERSION}" --activate \
    && node --version \
    && pnpm --version

RUN useradd --create-home --shell /bin/bash --uid 1000 oddjobs \
    && mkdir -p /home/oddjobs/.gradle /home/oddjobs/.cache/corepack /var/log/oddjobs /workspace/oddjobs \
    && chown -R oddjobs:oddjobs /home/oddjobs /var/log/oddjobs /workspace

WORKDIR /workspace/oddjobs

EXPOSE 9991 9992 9994

CMD ["bash", "scripts/dev-container-start.sh"]
