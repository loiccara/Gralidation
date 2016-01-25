package org.cara.utils.gralidation

import spock.lang.Specification
import static org.cara.utils.gralidation.GralidationEnum.*


class GralidationEnumSpec extends Specification {

    def "nullable is checked"() {
        given: "a dummy object"
        DummyObject foo1 = new DummyObject(name: null, aDummyList: null)
        DummyObject foo2 = new DummyObject(name: "dummyName", aDummyList: null)

        expect:
        NULLABLE.control.call(foo1.name, true)
        !NULLABLE.control.call(foo1.name, false)
        NULLABLE.control.call(foo2.name, true)
        NULLABLE.control.call(foo2.name, false)
    }

    def "maxlength is checked"(){
        given:
        DummyObject foo1 = new DummyObject(name: "dummyName", aDummyList: [])
        DummyObject foo2 = new DummyObject(name: "dummyName", aDummyList: [1,2])

        expect:
        MAXLENGTH.control.call(foo1.aDummyList, 1)
        MAXLENGTH.control.call(foo2.aDummyList, 2)
        !MAXLENGTH.control.call(foo2.aDummyList, 1)
    }

    def "minlength is checked"(){
        given:
        DummyObject foo1 = new DummyObject(name: "dummyName", aDummyList: [])
        DummyObject foo2 = new DummyObject(name: "dummyName", aDummyList: [1,2])

        expect:
        !MINLENGTH.control.call(foo1.aDummyList, 1)
        MINLENGTH.control.call(foo2.aDummyList, 2)
        !MINLENGTH.control.call(foo2.aDummyList, 3)
    }

    class DummyObject{
        String name
        List aDummyList

        static def constraints=[
            "name":["nullable":"true"],
            "aDummyList":["nullable":"false"]
        ]
    }


}