package org.cara.utils.gralidation

import spock.lang.Specification

import static org.cara.utils.gralidation.GralidationEnum.*

class GralidationEnumSpec extends Specification {

    def "nullable is checked"() {
        given:
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
        MAXSIZE.control.call(foo1.aDummyList, 1)
        MAXSIZE.control.call(foo2.aDummyList, 2)
        !MAXSIZE.control.call(foo2.aDummyList, 1)
    }

    def "minlength is checked"(){
        given:
        DummyObject foo1 = new DummyObject(name: "dummyName", aDummyList: [])
        DummyObject foo2 = new DummyObject(name: "dummyName", aDummyList: [1,2])

        expect:
        !MINSIZE.control.call(foo1.aDummyList, 1)
        MINSIZE.control.call(foo2.aDummyList, 2)
        !MINSIZE.control.call(foo2.aDummyList, 3)
    }

    def "blank is checked"(){
        given:
        DummyObject foo1 = new DummyObject(name: "", aDummyList: [])
        DummyObject foo2 = new DummyObject(name: "dummyName", aDummyList: [])

        expect:
        BLANK.control.call(foo1.name, true)
        !BLANK.control.call(foo1.name, false)
        BLANK.control.call(foo2.name, true)
        BLANK.control.call(foo2.name, false)
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