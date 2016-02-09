package org.cara.utils.gralidation

import spock.lang.Specification

import static org.cara.utils.gralidation.GralidationEnum.*

class GralidationEnumSpec extends Specification {

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

    def "inlist is checked"(){
        given:
        List superHeroWithPowers = ["SUPERMAN", "AQUAMAN", "RAYMAN", "CYCLOP"]
        DummyObject withInvalidData = new DummyObject(favouriteSuperHeroWithPowers:"BATMAN")
        DummyObject withValidData = new DummyObject(favouriteSuperHeroWithPowers:"SUPERMAN")

        List validInt = [1,2,3,4]
        DummyObject withInvalidData2 = new DummyObject(limbs: 0)
        DummyObject withValidData2 = new DummyObject(limbs: 4)

        expect:
        !INLIST.control.call(withInvalidData.favouriteSuperHeroWithPowers, superHeroWithPowers)
        INLIST.control.call(withValidData.favouriteSuperHeroWithPowers, superHeroWithPowers)
        !INLIST.control.call(withInvalidData2.limbs, validInt)
        INLIST.control.call(withValidData2.limbs, validInt)
    }

    def "max is checked"() {
        given:
        DummyObject validOneMissingArm = new DummyObject(name: "dummyName", aDummyList: [], limbs: 3)
        DummyObject validObject = new DummyObject(name: "dummyName", aDummyList: [], limbs: 4)
        DummyObject invalidTooManyArms = new DummyObject(name: "dummyName", aDummyList: [], limbs: 5)

        expect:
        MAX.control.call(validOneMissingArm.limbs, 4)
        MAX.control.call(validObject.limbs, 4)
        !MAX.control.call(invalidTooManyArms.limbs, 4)
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

    def "min is checked"() {
        given:
        DummyObject validEnoughNeurons = new DummyObject(name: "dummyName", aDummyList: [], neurons: 2323846233)
        DummyObject invalidNotEnoughNeurons = new DummyObject(name: "dummyName", aDummyList: [], neurons: 0)

        expect:
        MIN.control.call(validEnoughNeurons.neurons, 1)
        !MIN.control.call(invalidNotEnoughNeurons.neurons, 1)
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

    def "notequal is checked"(){
        given:
        DummyObject foo1 = new DummyObject(name: "BATMAN")
        DummyObject foo2 = new DummyObject(name: "batman")
        DummyObject foo3 = new DummyObject(name: "superman")

        expect:
        !NOTEQUAL.control.call(foo1.name, "BATMAN")
        NOTEQUAL.control.call(foo2.name, "BATMAN")
        NOTEQUAL.control.call(foo3.name, "BATMAN")
    }

    def "nullable is checked"(){
        given:
        DummyObject foo1 = new DummyObject(name: null, aDummyList: null)
        DummyObject foo2 = new DummyObject(name: "dummyName", aDummyList: null)

        expect:
        NULLABLE.control.call(foo1.name, true)
        !NULLABLE.control.call(foo1.name, false)
        NULLABLE.control.call(foo2.name, true)
        NULLABLE.control.call(foo2.name, false)
    }

    class DummyObject{
        String name
        List aDummyList
        int children
        long neurons
        int limbs
        String favouriteSuperHeroWithPowers
    }


}