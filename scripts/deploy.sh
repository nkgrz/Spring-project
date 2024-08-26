#!/bin/bash

set -e

APP_NAME="learning-spring"
WAR_FILE="${APP_NAME}-0.0.1-SNAPSHOT.war"
REMOTE_USER="parallels"
REMOTE_HOST="10.211.55.10"
REMOTE_DIR="/home/parallels/app"
APP_PATH="$REMOTE_DIR/$WAR_FILE"
LOG_FILE="$REMOTE_DIR/log.txt"

log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1"
}

log "Building project with Maven..."
mvn clean package

log "Copying WAR file to remote server..."
scp "target/$WAR_FILE" "$REMOTE_USER@$REMOTE_HOST:$REMOTE_DIR"

log "Restarting application on remote server..."
ssh "$REMOTE_USER@$REMOTE_HOST" << EOF
    sudo pkill -f 'java -jar $APP_PATH' || true
    nohup java -jar "$APP_PATH" > "$LOG_FILE" 2>&1 &
EOF

log "Deployment completed. Application is running on $REMOTE_HOST."

log "Goodbye!"
