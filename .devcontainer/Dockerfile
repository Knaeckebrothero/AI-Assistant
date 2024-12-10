# Start from a base that has Java 17 installed
FROM mcr.microsoft.com/devcontainers/java:17

# Install Gradle
# We'll use SDKMAN for convenience, but you can also use apt or other approaches
USER vscode
RUN curl -s "https://get.sdkman.io" | bash \
    && bash -c "source $HOME/.sdkman/bin/sdkman-init.sh && sdk install gradle" \
    && echo "source $HOME/.sdkman/bin/sdkman-init.sh" >> ~/.bashrc

# Switch back to root for any final system-wide install steps, then return to vscode
USER root
RUN apt-get update && apt-get install -y \
    docker.io \
    && rm -rf /var/lib/apt/lists/*
USER vscode

# By default, the devcontainer.json mounts the Docker socket and configures the environment. 
# This Dockerfile primarily ensures you have a suitable Java and Gradle environment.
