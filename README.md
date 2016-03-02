# What is this repository for? #

Gralidation helps you not to waste time while validating data. Inspired by Gorm, powered by Groovy, developed for developers who do not want to do manual validation. 

With this library, you can validate Objects and Maps, by creating a map of constraints.

***Object version:***
```
#!groovy
Class superHero

String name
List powers
String weakness

Map constraints = [
  name:[nullable:false, blank:false],
]

```
***Map version:***

```
#!groovy

Map batman = [name:batman, powers:[], weakness:""]
Map superman = [name:superman, powers:["Frozen breath", "Lasers", "Fly", "Amazing strength"], weakness:"kryptonite"]

Map constraints = [
  name:[nullable:false, blank:false],
]

```

You can get the validation result by:

```
#!groovy

GralidationResult gralidationResult = Gralidator.gralidate(batman)
```

Version 0.1

# How do I get set up? ##

### Installation ###
1. Option 1: add the gradle dependency
COMING SOON 

2. Option 2: download the jar in your project. I do not recommend this solution, as it makes it impossible to switch to a better version easily. still necessary as long as this library isn't available for gradle or maven builds.

### Configuration ###
There are dependencies on Groovy 2.4 and Spock. 

To ensure that your validation is read properly, you need to create a map of constraints. This map needs to be in your Object class in case of an object validation. E.g.:

Map constraints = [
  name:[nullable:true, blank:false],
  age:[nullable:false]
]

The result is a new object, containing:
- a boolean isValid, true if the validation is correct
- a list of errors, containing all the validation errors in case of a failure during the validation.

### Contribution guidelines ### COMING SOON

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

Loic Cara
cara.loic.pro@gmail.com