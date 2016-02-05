# What is this repository for? #

Gralidation helps you not to waste time while validating data. Inspired by Gorm, powered by Groovy, developed for developers who do not want to do manual validation. 

Version 0.1

# How do I get set up? ##

### Installation ###
1. Option 1: add the gradle dependency
COMING SOON 

2. Option 2: download the jar in your project
I do not recommend this solution, as it makes it impossible to switch to a better version easily. still necessary as long as this library isn't available for gradle or maven builds.

### Configuration ###
There are dependencies on Groovy 2.4 and Spock. 

To ensure that your validation is read properly, you need to have in your objects the following properties:

1. Map constraints
constrains is a map of properties and controls. As an example:

Map constraints = [
  name:[nullable:true, blank:false],
  age:[nullable:false]
]

2. List errors
errors can be set at [] during the object creation. This list of errors will be set at [] at each execution.


### Contribution guidelines ### COMING SOON

* Writing tests
* Code review
* Other guidelines

### Who do I talk to? ###

Loic Cara
cara.loic@gmail.com