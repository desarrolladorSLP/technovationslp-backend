# technovationslp-backend
Backend for Technovation app for San Luis Potosí, México community

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

> Note: Once issue #4 is done we'll be able to use a profile to use a fixed 
token for dev tests locally

> Note: The real configuration is provided vía environment variables and can't be shared due to 
security risks

## Testing

To test the application you can use the [postman collection provided](src/test/postman-collection)    






 
