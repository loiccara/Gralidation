package org.cara.utils.gralidation

import spock.lang.Specification

/**
 * Created by loic on 24/01/2016.
 */
class GralidatorSpec extends Specification {

    class DummyObjectWithoutConstraints{
        String name
        List dummyList
    }

    class DummyObjectWithEmptyConstraints{
        String name
        List dummyList

        static def constraints = [:]
        List errors
    }

    class DummyObjectWithConstraints{
        String name
        List dummyList

        static def constraints = [
            name:[nullable:false],
            dummyList:[maxlength:5, minlength:3]
        ]
        List errors
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
        objectWithConstraints.gralidate()

        then:""



    }
}
