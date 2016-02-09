package org.cara.utils.gralidation

import spock.lang.Specification

class GralidatorSpec extends Specification {


    /************************* OBJECTS VALIDATION *************************/
    class DummyObjectWithoutConstraints{
        String name
        List dummyList
    }

    class DummyObjectWithEmptyConstraints{
        String name
        List dummyList

        static def constraints = [:]
    }

    class DummyObjectWithConstraints{
        String name
        List dummyList

        static def constraints = [
            name:[nullable:false],
            dummyList:[maxsize:5, minsize:3]
        ]
    }

    def "a gralidation without constraints throws an exception"(){
        given:
        DummyObjectWithoutConstraints foo1 = new DummyObjectWithoutConstraints()

        when:
        Gralidator.gralidate(foo1)

        then:
        thrown(MissingPropertyException)
    }

    def "a gralidation with empty constraints returns true"(){
        given:
        DummyObjectWithEmptyConstraints foo1 = new DummyObjectWithEmptyConstraints()

        expect:
        Gralidator.gralidate(foo1)
    }

    def "a gralidation with constraints checks all the values for each property"(){
        given:
        DummyObjectWithConstraints foo1 = new DummyObjectWithConstraints(name:"test", dummyList: [1,2,3,4])

        expect:
        Gralidator.gralidate(foo1)
    }

    def "Gralidator adds a new method to Object on init"(){
        given:""
        Object object1 = new Object()

        when:"trying to gralidate before init method"
        object1.gralidate()

        then:"the gralidate method is missing"
        thrown(MissingMethodException)

        when:"Init is done"
        Gralidator.initGralidator()

        and:"a gralidation is done on an object without constraints"
        Object object2 = new Object()
        object2.gralidate()

        then:"the constraints property is missing"
        thrown(MissingPropertyException)

        when:"a gralidation is done on an object with constraints"
        DummyObjectWithConstraints objectWithConstraints = new DummyObjectWithConstraints()
        GralidationResult gralidationResult = objectWithConstraints.gralidate()

        then:"the gralidation is executed"
        !gralidationResult.isValid
        gralidationResult.errors.size()==2
    }

    /************************* MAPS VALIDATION **************************/

    def "should validate even if both collections are null"(){
        given:
        Map myMap
        Map constraints

        when:
        GralidationResult gralidationResult = Gralidator.gralidate(myMap, constraints, true)

        then:
        gralidationResult.isValid
    }

    def "should tell when a property doesn't exist"(){
        given:
        Map myMap
        Map constraints = [batmanData:[nullable:false]]

        when:
        GralidationResult gralidationResult = Gralidator.gralidate(myMap, constraints, true)

        then:
        !gralidationResult.isValid
        gralidationResult.errors.size() == 1
        gralidationResult.errors[0].contains("inexistantProperty")
    }

    def "should allow inexistant properties"(){
        given:
        Map myMap
        Map constraints = [batmanData:[nullable:false]]

        when:
        GralidationResult gralidationResult = Gralidator.gralidate(myMap, constraints, false)

        then:
        gralidationResult.isValid
    }

    def "should gralidate successfully a Map when both collections are correct"(){
        given:
        Map validDataMap = ["superman":"I can fly and destroy BATMAN", "batman":"DO YOU BLEED?", "aquaman":null]
        Map invalidDataMap = ["superman":"I can fly and destroy BATMAN", "batman":null, "aquaman":"HEY! I exist and I'm cool!"]

        Map constraints = [batman:[nullable:false], aquaman:[nullable:true]]

        when:
        GralidationResult validGralidationResult = Gralidator.gralidate(validDataMap, constraints, true)

        then:
        validGralidationResult.isValid

        when:
        GralidationResult invalidGralidationResult = Gralidator.gralidate(invalidDataMap, constraints, true)

        then:
        !invalidGralidationResult.isValid
        invalidGralidationResult.errors.size() == 1
    }

    /************************* LISTS VALIDATION **************************/

}
