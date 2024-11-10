#!/bin/bash
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color
WILDFLY_HOME="/opt/homebrew/opt/wildfly-as/libexec"
echo -e "${YELLOW}starting build and deploy project ${NC}"
if [ ! -f "pom.xml" ]; then
    echo -e "${RED}pom.xml is not found${NC}"
    exit 1
fi
PROJECT_NAME=$(mvn help:evaluate -Dexpression=project.artifactId -q -DforceStdout)
if [ -z "$PROJECT_NAME" ]; then
    echo -e "${RED}failed to get project name${NC}"
    exit 1
fi
echo -e "${YELLOW}Building $PROJECT_NAME...${NC}"
mvn clean package
if [ $? -ne 0 ]; then
    echo -e "${RED}failed to run mvn clean package${NC}"
    exit 1
fi
if ! pgrep -f "wildfly" > /dev/null; then
    echo -e "${YELLOW}wildfly is not running or not found${NC}"
    $WILDFLY_HOME/bin/standalone.sh &
    echo -e "${YELLOW}starting wildfly...${NC}"
    sleep 10
fi
WAR_PATH="target/$PROJECT_NAME.war"
if [ ! -f "$WAR_PATH" ]; then
    echo -e "${RED}WAR is not found $WAR_PATH${NC}"
    exit 1
fi
echo -e "${YELLOW}Deployment is started...${NC}"
rm -f "$WILDFLY_HOME/standalone/deployments/$PROJECT_NAME.war*"
cp "$WAR_PATH" "$WILDFLY_HOME/standalone/deployments/"
echo -e "${YELLOW}Hold on...${NC}"
sleep 5
if [ -f "$WILDFLY_HOME/standalone/deployments/$PROJECT_NAME.war.deployed" ]; then
    echo -e "${GREEN}Done!${NC}"
    echo -e "${GREEN}http://localhost:8081/$PROJECT_NAME${NC}"
else
    echo -e "${RED}Failed to deploy please check${NC}"
    echo -e "${YELLOW}$WILDFLY_HOME/standalone/log/*${NC}"
    echo -e "${YELLOW}$WILDFLY_HOME/standalone/deployments/*${NC}"
    exit 1
fi
