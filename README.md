# technovationslp-backend
Backend for Technovation app for San Luis Potosí, México community

To know more about the project take a look to our [wiki](https://github.com/desarrolladorSLP/technovationslp-backend/wiki)

## Build

To build the application it is required to have maven installed then execute:

`mvn clean package`

## Run

To run the application execute:
```
java -DAUTHENTICATION_WHITELIST=<comma separated list of accepted emails> \
     -DCLIENT_ID=<any string to use as client id for the application authentication> \
     -DCLIENT_SECRET=<any string to use as password for the application authentication> \
     -DJWT_PUBLIC_KEY=<public key for JWT encoded as base64> \
     -DJWT_PRIVATE_KEY=<private key for JWT encoded as base64> \
     -DFIREBASE_DATABASE_URL=https://<project name>.firebaseio.com \
     -DFIREBASE_CONFIG=<json object provided by firebase for service account authentication encoded as base64 > \
      -jar target/technovation-backend-<version>.jar
```
   
It is recommend to add (anywhere between `java` and `-jar`)

```
-Xms256m -Xmx256m -XX:MaxMetaspaceSize=256m 
```

To save some resources when it is run locally 

### JWT configuration

To generate pair of keys for JWT you can follow the steps bellow:

1. `openssl genrsa -out jwt.pem` to generate the pair of keys
2. `openssl rsa -in jwt.pem ` will show the private key
3. `openssl rsa -in jwt.pem -pubout` will show the public key

### Firebase configuration

In order to fill the parameters `FIREBASE_DATABASE_URL` and `FIREBASE_CONFIG` you'll need to create 
a firebase project under https://console.firebase.google.com 

To get `FIREBASE_CONFIG` value go to `Project configuration`/`Service accounts` and then click on 
`Generate new private key`. You'll get a json file, copy the content and encoded it as base64. Use the
encoded value for the parameter.

> Note: Read first the section `Dev environment` on this document before create any firebase project 
it can be not needed at all

> Note: The real configuration is provided vía environment variables and can't be shared due to 
security reasons

## Testing

To test the application you can use the [postman collection provided](src/test/postman-collection)    

### Dev environment

It has been created a spring profile so devs don't need to worry about a real firebase token to authenticate
requests. In order to use this feature run the application
adding

```--spring.profiles.active=fake-token-granter```

The values that can be used as the tokenId are described in the file [application-fake-token-granter.yml](src/main/resources/application-fake-token-granter.yml)

Use those tokens for the parameter `firebase-token-id` during login to the endpoint `oauth/token`

# Collaboration

First, thanks to be interested in collaborate on this project. In order to have an idea of the big picture of the project we highly recommend to take a look first to our [wiki](https://github.com/desarrolladorSLP/technovationslp-backend/wiki) 

This project is divided in backend (this repo), an iOS app, an android app and an angular front end your collaboration on
any of them will be greatly appreciated

Check other repos for guidelines for every specific project.

## Guidelines for backend

1. You can take an existent issue or create a new one in order to start working with us. Please anything you want to do must be associated to a well described issue
2. Create a branch for the issue you will work on and once you have tested locally open a PR for code review.
3. During PR we'll check:
    1. You have included unit tests for your changes
    2. There is no unused or commented code
    3. javadocs are not needed at all but code must be auto-descriptive
    4. Avoid big methods/classes
    5. Have in mind [SOLID](https://scotch.io/bar-talk/s-o-l-i-d-the-first-five-principles-of-object-oriented-design) 
4. If you want to add unit tests for existing code only that will be of course appreciated. You can do it but also start please writing an issue for it.        
    





 
