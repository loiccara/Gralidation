package org.cara.utils.gralidation

import spock.lang.Specification

import static org.cara.utils.gralidation.GralidationEnum.*

class GralidationEnumSpec extends Specification {

    def "blank is checked"(){
        given:
        DummyObject foo1 = new DummyObject(name: "", aDummyList: [])
        DummyObject foo2 = new DummyObject(name: "dummyName", aDummyList: [])

        expect:
        BLANK.control.call(foo1.name, true).isValid
        !BLANK.control.call(foo1.name, false).isValid
        BLANK.control.call(foo2.name, true).isValid
        BLANK.control.call(foo2.name, false).isValid
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
        !INLIST.control.call(withInvalidData.favouriteSuperHeroWithPowers, superHeroWithPowers).isValid
        INLIST.control.call(withValidData.favouriteSuperHeroWithPowers, superHeroWithPowers).isValid
        !INLIST.control.call(withInvalidData2.limbs, validInt).isValid
        INLIST.control.call(withValidData2.limbs, validInt).isValid
    }

    def "max is checked"() {
        given:
        DummyObject validOneMissingArm = new DummyObject(name: "dummyName", aDummyList: [], limbs: 3)
        DummyObject validObject = new DummyObject(name: "dummyName", aDummyList: [], limbs: 4)
        DummyObject invalidTooManyArms = new DummyObject(name: "dummyName", aDummyList: [], limbs: 5)

        expect:
        MAX.control.call(validOneMissingArm.limbs, 4).isValid
        MAX.control.call(validObject.limbs, 4).isValid
        !MAX.control.call(invalidTooManyArms.limbs, 4).isValid
    }

    def "maxsize is checked"(){
        given:
        DummyObject foo1 = new DummyObject(name: "dummyName", aDummyList: [])
        DummyObject foo2 = new DummyObject(name: "dummyName", aDummyList: [1,2])

        expect:
        MAXSIZE.control.call(foo1.aDummyList, 1).isValid
        MAXSIZE.control.call(foo2.aDummyList, 2).isValid
        !MAXSIZE.control.call(foo2.aDummyList, 1).isValid
    }

    def "min is checked"() {
        given:
        DummyObject validEnoughNeurons = new DummyObject(name: "dummyName", aDummyList: [], neurons: 2323846233)
        DummyObject invalidNotEnoughNeurons = new DummyObject(name: "dummyName", aDummyList: [], neurons: 0)

        expect:
        MIN.control.call(validEnoughNeurons.neurons, 1).isValid
        !MIN.control.call(invalidNotEnoughNeurons.neurons, 1).isValid
    }

    def "minsize is checked"(){
        given:
        DummyObject foo1 = new DummyObject(name: "dummyName", aDummyList: [])
        DummyObject foo2 = new DummyObject(name: "dummyName", aDummyList: [1,2])

        expect:
        !MINSIZE.control.call(foo1.aDummyList, 1).isValid
        MINSIZE.control.call(foo2.aDummyList, 2).isValid
        !MINSIZE.control.call(foo2.aDummyList, 3).isValid
    }

    def "notequal is checked"(){
        given:
        DummyObject foo1 = new DummyObject(name: "BATMAN")
        DummyObject foo2 = new DummyObject(name: "batman")
        DummyObject foo3 = new DummyObject(name: "superman")

        expect:
        !NOTEQUAL.control.call(foo1.name, "BATMAN").isValid
        NOTEQUAL.control.call(foo2.name, "BATMAN").isValid
        NOTEQUAL.control.call(foo3.name, "BATMAN").isValid
    }

    def "nullable is checked"(){
        given:
        DummyObject foo1 = new DummyObject(name: null, aDummyList: null)
        DummyObject foo2 = new DummyObject(name: "dummyName", aDummyList: null)

        expect:
        NULLABLE.control.call(foo1.name, true).isValid
        !NULLABLE.control.call(foo1.name, false).isValid
        NULLABLE.control.call(foo2.name, true).isValid
        NULLABLE.control.call(foo2.name, false).isValid
    }

    def "each is checked"(){
        given:
        DummyObject foo1 = new DummyObject(aDummyList: ["BATMAN", "SUPERMAN"])
        DummyObject foo2 = new DummyObject(aDummyList: ["BATMAN", "SUPERMAN", "SUPERFRENCHMAN"])
        DummyObject foo3 = new DummyObject(aDummyList: ["BATMAN", "SUPERMAN", "Super French Man", "LOIC THE SUPER HERO FLYING THROUGH THE SKY"])

        Map nullConstraints = null
        Map emptyConstraints = [:]
        Map constraints = [maxsize:10]

        when:
        GralidationResult result0 = EACH.control.call(foo1.aDummyList, nullConstraints)
        then:
        result0.isValid

        when:
        GralidationResult result1 = EACH.control.call(foo1.aDummyList, emptyConstraints)
        then:
        result1.isValid

        when:
        GralidationResult result2 = EACH.control.call(foo1.aDummyList, constraints)
        then:
        result2.isValid

        when:
        GralidationResult result3 = EACH.control.call(foo2.aDummyList, emptyConstraints)
        then:
        result3.isValid

        when:
        GralidationResult result4 = EACH.control.call(foo2.aDummyList, constraints)
        then:
        !result4.isValid
        result4.errors.size() == 1

        when:
        GralidationResult result5 = EACH.control.call(foo3.aDummyList, constraints)
        then:
        !result5.isValid
        result5.errors.size() == 2
    }

    def "eachkey is checked"() {
        given:
        DummyObject foo1 = new DummyObject(dailyHours:[MONDAY:"from 08:00 to 18:00", TUESDAY:"from 08:00 to 19:00"])
        DummyObject foo2 = new DummyObject(dailyHours:[MONDAY:"from 08:00 to 18:00", NOVEMBER:"from 08:00 to 19:00"])
        List daysOfTheWeek = ["MONDAY", "TUESDAY", "WEDNESDAY"]

        expect:
        EACHKEY.control.call(foo1.dailyHours, [inlist:daysOfTheWeek]).isValid
        !EACHKEY.control.call(foo2.dailyHours, [inlist:daysOfTheWeek]).isValid
    }

    class DummyObject{
        String name
        List aDummyList
        int children
        long neurons
        int limbs
        String favouriteSuperHeroWithPowers
        Map<String, String> dailyHours
    }
}