#!/bin/bash

# create-javaee-project.sh
echo "🚀 Création d'un nouveau projet Java EE 8"
echo "----------------------------------------"

# Demander les informations du projet
read -p "Nom du projet (ex: MonProjet): " PROJECT_NAME
read -p "Group ID (ex: com.example): " GROUP_ID
read -p "Package principal (ex: com.example.monprojet): " MAIN_PACKAGE

# Convertir le package en structure de répertoires
PACKAGE_PATH=$(echo $MAIN_PACKAGE | sed 's/\./\//g')

# Créer la structure du projet
echo "📁 Création de la structure du projet..."
mkdir -p $PROJECT_NAME/{src/main/java/$PACKAGE_PATH/{servlets,beans},src/main/resources,src/main/webapp/{WEB-INF,css,js}}
mkdir -p $PROJECT_NAME/.vscode


cat > $PROJECT_NAME/deploy.sh << 'EOF'
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
EOF

cat > $PROJECT_NAME/src/main/webapp/index.jsp << 'EOF'
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Accueil</title>
    <link rel="stylesheet" href="css/style.css">
</head>
<body>
    <div class="container">
        <h1>Hello</h1>
    </div>
    
    <script src="js/main.js"></script>
</body>
</html>
EOF

# css
cat > $PROJECT_NAME/src/main/webapp/css/style.css << 'EOF'

EOF

# js
cat > $PROJECT_NAME/src/main/webapp/js/main.js << 'EOF'

EOF

# vscode
cat > $PROJECT_NAME/.vscode/tasks.json << 'EOF'
{
    "version": "2.0.0",
    "tasks": [
        {
            "label": "Build",
            "type": "shell",
            "command": "mvn clean package",
            "group": {
                "kind": "build",
                "isDefault": true
            }
        }
    ]
}
EOF

# Update the pom.xml section in your new-project.sh
cat > $PROJECT_NAME/pom.xml << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>GROUP_ID_PLACEHOLDER</groupId>
    <artifactId>ARTIFACT_ID_PLACEHOLDER</artifactId>
    <version>1.0-SNAPSHOT</version>
    <packaging>war</packaging>

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <maven.compiler.target>23</maven.compiler.target>
        <maven.compiler.source>23</maven.compiler.source>
        <junit.version>5.11.0-M2</junit.version>
    </properties>

    <dependencies>
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>6.0.0</version>
            <scope>provided</scope>
        </dependency>
        
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-api</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>

        <dependency>
           <groupId>jakarta.platform</groupId>
           <artifactId>jakarta.jakartaee-api</artifactId>
           <version>9.1.0</version> <!-- or another version -->
           <scope>provided</scope>
        </dependency>

        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>${junit.version}</version>
            <scope>test</scope>
        </dependency>

        <!-- MySQL Connector -->
        <dependency>
            <groupId>com.mysql</groupId>
            <artifactId>mysql-connector-j</artifactId>
            <version>8.3.0</version>
            <scope>provided</scope>
        </dependency>

    </dependencies>

    <build>
        <finalName>${project.artifactId}</finalName>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-war-plugin</artifactId>
                <version>3.4.0</version>
                <configuration>
                    <failOnMissingWebXml>false</failOnMissingWebXml>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
EOF

cat > $PROJECT_NAME/src/main/webapp/WEB-INF/web.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<web-app version="3.1"
         xmlns="http://xmlns.jcp.org/xml/ns/javaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://xmlns.jcp.org/xml/ns/javaee http://xmlns.jcp.org/xml/ns/javaee/web-app_3_1.xsd">

    <servlet>
       <servlet-name>HelloServlet</servlet-name>
       <servlet-class>$MAIN_PACKAGE.servlets.HelloServlet</servlet-class>
   </servlet>
   <servlet-mapping>
       <servlet-name>HelloServlet</servlet-name>
       <url-pattern>/hello</url-pattern>
   </servlet-mapping>
</web-app>
EOF

# Update jboss-web.xml
cat > $PROJECT_NAME/src/main/webapp/WEB-INF/jboss-web.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<jboss-web xmlns="http://www.jboss.org/schema/jbossas/4.0">
    <context-root>/$PROJECT_NAME</context-root>
</jboss-web>
EOF

# Update the jboss-deployment-structure.xml
cat > $PROJECT_NAME/src/main/webapp/WEB-INF/jboss-deployment-structure.xml << EOF
<?xml version="1.0" encoding="UTF-8"?>
<jboss-deployment-structure xmlns="urn:jboss:deployment-structure:1.3">
    <deployment>
        <dependencies>
            <module name="jakarta.servlet.api" export="true"/>
        </dependencies>
    </deployment>
</jboss-deployment-structure>
EOF

# Update HelloServlet.java to use Jakarta EE imports
cat > $PROJECT_NAME/src/main/java/$PACKAGE_PATH/servlets/HelloServlet.java << EOF
package $MAIN_PACKAGE.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        
        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Hello Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Hello from Jakarta EE Servlet!</h1>");
            out.println("<p>Context Path: " + request.getContextPath() + "</p>");
            out.println("<p>Servlet Path: " + request.getServletPath() + "</p>");
            out.println("</body>");
            out.println("</html>");
        }
    }
}
EOF

#
sed -i '' "s/DISPLAY_NAME_PLACEHOLDER/$PROJECT_NAME/g" $PROJECT_NAME/src/main/webapp/WEB-INF/web.xml
sed -i '' "s/PACKAGE_PLACEHOLDER/$MAIN_PACKAGE/g" $PROJECT_NAME/src/main/webapp/WEB-INF/web.xml
sed -i '' "s/GROUP_ID_PLACEHOLDER/$GROUP_ID/g" $PROJECT_NAME/pom.xml
sed -i '' "s/ARTIFACT_ID_PLACEHOLDER/$PROJECT_NAME/g" $PROJECT_NAME/pom.xml

chmod 755 "$PROJECT_NAME/deploy.sh"

echo "✅ Projet créé avec succès !"
echo "Pour commencer :"
echo "1. cd $PROJECT_NAME"
echo "2. mvn clean package"

code "./$PROJECT_NAME"