## Quick Start
### Setup project, create credentials and setup redirect url
* https://console.developers.google.com/
* get "client ID" and "client secret"
```
$ gradle run -Dclient_id={ClinetID}
```

## Steps for authenticating user using "server flow".

### Send redirect response to authentication endpoint
* retrieve authorization url by discovery url https://accounts.google.com/.well-known/openid-configuration
* get url by key "authorization_endpoint"


## References
* https://developers.google.com/identity/protocols/OpenIDConnect