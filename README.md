## Steps
```
$ gradle init --type=java-library
```
* set maven central repository
```
mavenCentral()
```
* Java version
```
sourceCompatibility = 1.8
targetCompatibility = 1.8
```
* App execution config
```
apply plugin: 'application'
mainClassName = "com.semaks.runner"
```
* Run
```
gradle run
```