## Getting Started

Follow these instructions to get started with the project:

### Prerequisites

- Git
- Maven
- Docker Desktop (for Windows and Mac users)

### Installation

1. **Clone the Repository**: Use Git to clone the repository to your local machine.
   
   ```bash
   git clone https://github.com/osmel1/hbase-docker.git
2. **Build the JAR file**: Navigate to the root directory of the cloned repository and use Maven to build the JAR file.

```bash
cd hbase-docker
mvn package
```
3. **Start Docker Desktop**: If you are a Windows or Mac user, ensure Docker Desktop is installed and running on your machine.

4. **Launch Docker Compose**: Open a terminal or command prompt, navigate to the root directory of the cloned repository, and execute the following command:

```bash
docker-compose up -d
```
