# Running and Building payroll-manager Spring Boot Application

## Prerequisites
1. Java 11 or higher
2. Maven (dependency management)

## Running the Application

## Using Maven
1. Clone the repository
```sh
git https://github.com/alexferreras/payroll-manager.git
```
2. Navigate to the root directory of the application.
```sh
 cd payroll-manager
```
3. Set the environment variables
   1. Create a file named .env in the root directory of the application.
   2. Add the following environment variables to the .env file.
    ```sh
    api_username=your_api_username
    api_password=your_api_password
    email_sender=your_email_sender
    email_password=your_email_password
    ````
4. Run the application
```sh
mvn spring-boot:run (using Maven).
```

#### The application will start on http://localhost:8080.

## Using Docker

1. Clone the repository
```sh
git https://github.com/alexferreras/payroll-manager.git
```
2. Navigate to the root directory of the application.
```sh
 cd payroll-manager
```
3. create jar file
```sh
mvn clean package
```
4. Build the docker image
```sh 
docker build -t payroll-manager .
```
5. Run container
```sh
 docker run -p 8080:8080 -e email_password=your_email_password -e email_sender=your_email_sender -e api_username=your_api_username -e api_password=your_api_password -e debug_mode=true payroll-manager:latest
````

#### The application will start on http://localhost:8080.

## Sending a POST Request
Open a terminal or command prompt.
1. Run the command
```sh
curl -X POST http://localhost:8080/payroll/upload -F "file=@path/to/file" -F "country=country_name" -F "company=company_name" -F "credentials=username+password"
```
Replace path/to/file with the path to the file you want to upload there is a DATA_EXAMPLE_REQUEST.csv in the root of the project, country_name with the name of the country (USA OR DO), company_name with the name of the company, and credentials with the username and password set up as api_username and api_password in the environment variable set up.

## Note: 
1. The file must be in CSV format.
2. curl is a command-line tool for sending HTTP requests.
   1. -X option to specify the request method (POST in this case)
   2. -F option to attach form data to the request.
   3. That's it. You should see a response from the server indicating whether the request was successful or not.