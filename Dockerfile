FROM debian:bullseye-slim as base

RUN apt-get update && apt-get install -y openjdk-17-jdk && apt-get -y install maven
# RUN apt-get update && apt-get install -y default-jre && apt-get -y install maven
RUN apt-get update && apt-get install -y wget

FROM base as runner

RUN mkdir "app"
COPY ./target/blockstreet*.jar /app/plugins/blockstreet.jar
RUN wget https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar -O /app/plugins/vault.jar
RUN wget https://dev.bukkit.org/projects/essentialsx/files/latest -O /app/plugins/essentialsx.jar

# RUN wget https://cdn.getbukkit.org/spigot/spigot-1.16.3.jar -O /app/spigot.jar
# RUN wget https://cdn.getbukkit.org/spigot/spigot-1.16.4.jar -O /app/spigot.jar
# RUN wget https://cdn.getbukkit.org/spigot/spigot-1.16.5.jar -O /app/spigot.jar

RUN wget https://download.getbukkit.org/spigot/spigot-1.20.4.jar -O /app/spigot.jar

# Create a non-privileged user that the app will run under.
# See https://docs.docker.com/go/dockerfile-user-best-practices/
ARG UID=10001
RUN adduser \
    --disabled-password \
    --gecos "" \
    --home "/nonexistent" \
    --shell "/sbin/nologin" \
    --no-create-home \
    --uid "${UID}" \
    appuser

RUN chown -R appuser:appuser /app

# Switch to the non-privileged user.
USER appuser

WORKDIR /app

# Accept the EULA
RUN echo "eula=true" > /app/eula.txt

EXPOSE 25565

# What the container should run when it is started.
ENTRYPOINT ["java", "-jar", "/app/spigot.jar"]
