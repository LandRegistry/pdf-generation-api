# PDF Generation API

This is the repository for the PDF Generation API used to collate the information required to generate a PDF document.

### Documentation

The API has been documented using swagger YAML files. 

The swagger files can be found under the [documentation](documentation) directory.

At present the documentation is not hooked into any viewer within the dev environment. To edit or view the 
documentation open the YAML file in swagger.io <http://editor.swagger.io>

### Certificates

To generate a signing certificate you can run the following commands

```bash
openssl genrsa -des3 -out server.key 1024
openssl req -new -key server.key -out server.csr
cp server.key server.key.org && openssl rsa -in server.key.org -out server.key
openssl x509 -req -days 730 -in server.csr -signkey server.key -out dev_signature.crt
openssl pkcs8 -topk8 -inform PEM -outform DER -in server.key -nocrypt > dev_signature.key
# to create a version of the key that can be pasted in a text file such as hiera
openssl enc -in dev_signature.key -a > dev_signature.key_txt
rm server.key server.csr server.key.org
```

When setting the certificate ensure the company name is set to HM Land Registry and the CN is set to LandRegistry.GOV.UK
