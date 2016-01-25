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
    }

    class DummyObjectWithConstraints{
        String name
        List dummyList

        static def constraints = [
                name:[nullable:false]]
    }

    def "a gralidation without constraints throws an exception"(){
        given:
        DummyObjectWithoutConstraints foo1 = new DummyObjectWithoutConstraints()

        when:
        Gralidator.gralidate(foo1)

        then:
        thrown(Exception)
    }

    def "a gralidation with empty constraints returns true"(){
        given:
        DummyObjectWithEmptyConstraints foo1 = new DummyObjectWithEmptyConstraints()

        expect:
        Gralidator.gralidate(foo1)
    }

    def "a gralidation with constraints checks all the values for each property"(){
        given:
        DummyObjectWithConstraints foo1 = new DummyObjectWithConstraints()

        expect:
        Gralidator.gralidate(foo1)
    }
}
