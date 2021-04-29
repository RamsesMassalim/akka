FROM openjdk:8-jre-alpine as stage0
LABEL snp-multi-stage="intermediate"
LABEL snp-multi-stage-id="3701d67a-4789-4ede-855d-4150f011e42e"
WORKDIR /opt/docker
COPY 1/opt /1/opt
COPY 2/opt /2/opt
USER root
RUN ["chmod", "-R", "u=rX,g=rX", "/1/opt/docker"]
RUN ["chmod", "-R", "u=rX,g=rX", "/2/opt/docker"]
RUN ["chmod", "u+x,g+x", "/1/opt/docker/bin/backend-akka"]

FROM openjdk:8-jre-alpine as mainstage
USER root
RUN id -u demiourgos728 1>/dev/null 2>&1 || (( getent group 0 1>/dev/null 2>&1 || ( type groupadd 1>/dev/null 2>&1 && groupadd -g 0 root || addgroup -g 0 -S root )) && ( type useradd 1>/dev/null 2>&1 && useradd --system --create-home --uid 1001 --gid 0 demiourgos728 || adduser -S -u 1001 -G root demiourgos728 ))
WORKDIR /opt/docker
COPY --from=stage0 --chown=demiourgos728:root /1/opt/docker /opt/docker
COPY --from=stage0 --chown=demiourgos728:root /2/opt/docker /opt/docker
USER 1001:0
ENTRYPOINT ["/opt/docker/bin/backend-akka"]
CMD ["/opt/docker/bin/backend-akka"]
