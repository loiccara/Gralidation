package org.cara.utils.gralidation

import spock.lang.Specification

import java.util.regex.Pattern

import static org.cara.utils.gralidation.GralidationEnum.*
import static org.cara.utils.gralidation.Gralidator.getERROR_CODE_PREFIX
import static org.cara.utils.gralidation.TypeCheck.*

class GralidationEnumSpec extends Specification {

    def "blank is checked"(){
        given:
        DummyObject foo1 = new DummyObject(name: "", aDummyList: [])
        DummyObject foo2 = new DummyObject(name: "dummyName", aDummyList: [])
        DummyObject foo3 = new DummyObject(name: "      ", aDummyList: [])
        DummyObject foo4 = new DummyObject(name: null, aDummyList: [])

        expect:
        BLANK.control.call("name", foo1.name, true).isValid
        !BLANK.control.call("name", foo1.name, false).isValid
        BLANK.control.call("name", foo2.name, true).isValid
        BLANK.control.call("name", foo2.name, false).isValid
        BLANK.control.call("name", foo3.name, true).isValid
        !BLANK.control.call("name", foo3.name, false).isValid
        BLANK.control.call("name", foo4.name, true).isValid
        BLANK.control.call("name", foo4.name, false).isValid
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
        !INLIST.control.call("favouriteSuperHeroWithPowers", withInvalidData.favouriteSuperHeroWithPowers, superHeroWithPowers).isValid
        INLIST.control.call("favouriteSuperHeroWithPowers", withValidData.favouriteSuperHeroWithPowers, superHeroWithPowers).isValid
        !INLIST.control.call("favouriteSuperHeroWithPowers", withInvalidData2.limbs, validInt).isValid
        INLIST.control.call("favouriteSuperHeroWithPowers", withValidData2.limbs, validInt).isValid
    }

    def "matches is checked"(){
        given:
        Pattern pattern1 =  ~/.{4}/

        expect:
        !MATCHES.control.call("name", "", pattern1).isValid
        MATCHES.control.call("name", "test", pattern1).isValid
    }

    def "max is checked"() {
        given:
        DummyObject validOneMissingArm = new DummyObject(limbs: 3)
        DummyObject validObject = new DummyObject(limbs: 4)
        DummyObject invalidTooManyArms = new DummyObject(limbs: 5)

        expect:
        MAX.control.call("limbs", validOneMissingArm.limbs, 4).isValid
        MAX.control.call("limbs", validObject.limbs, 4).isValid
        !MAX.control.call("limbs", invalidTooManyArms.limbs, 4).isValid
    }

    def "maxsize is checked"(){
        given:
        DummyObject foo1 = new DummyObject(aDummyList: [])
        DummyObject foo2 = new DummyObject(aDummyList: [1,2])

        expect:
        MAXSIZE.control.call("aDummyList", foo1.aDummyList, 1).isValid
        MAXSIZE.control.call("aDummyList", foo2.aDummyList, 2).isValid
        !MAXSIZE.control.call("aDummyList", foo2.aDummyList, 1).isValid
    }

    def "min is checked"() {
        given:
        DummyObject validEnoughNeurons = new DummyObject(neurons: 2323846233)
        DummyObject invalidNotEnoughNeurons = new DummyObject(neurons: 0)

        expect:
        MIN.control.call("neurons", validEnoughNeurons.neurons, 1).isValid
        !MIN.control.call("neurons", invalidNotEnoughNeurons.neurons, 1).isValid
    }

    def "minsize is checked"(){
        given:
        DummyObject foo1 = new DummyObject(aDummyList: [])
        DummyObject foo2 = new DummyObject(aDummyList: [1,2])

        expect:
        !MINSIZE.control.call("aDummyList", foo1.aDummyList, 1).isValid
        MINSIZE.control.call("aDummyList", foo2.aDummyList, 2).isValid
        !MINSIZE.control.call("aDummyList", foo2.aDummyList, 3).isValid
    }

    def "notequal is checked"(){
        given:
        DummyObject foo1 = new DummyObject(name: "BATMAN")
        DummyObject foo2 = new DummyObject(name: "batman")
        DummyObject foo3 = new DummyObject(name: "superman")

        expect:
        !NOTEQUAL.control.call("name", foo1.name, "BATMAN").isValid
        NOTEQUAL.control.call("name", foo2.name, "BATMAN").isValid
        NOTEQUAL.control.call("name", foo3.name, "BATMAN").isValid
    }

    def "nullable is checked"(){
        given:
        DummyObject foo1 = new DummyObject(name: null)
        DummyObject foo2 = new DummyObject(name: "dummyName")

        expect:
        NULLABLE.control.call("name", foo1.name, true).isValid
        !NULLABLE.control.call("name", foo1.name, false).isValid
        NULLABLE.control.call("name", foo2.name, true).isValid
        NULLABLE.control.call("name", foo2.name, false).isValid
    }

    def "type is checked"(String value, TypeCheck typeCheck, boolean isValid){
        expect:
        TYPE.control.call("name", value, typeCheck).isValid == isValid

        where:
        value   |   typeCheck   |   isValid
        "5"     |   BIGDECIMAL  |   true
        "5"     |   BIGINTEGER  |   true
        "5"     |   DOUBLE      |   true
        "5"     |   FLOAT       |   true
        "5"     |   INTEGER     |   true
        "5"     |   LONG        |   true
        "5"     |   NUMBER      |   true
        "TEST"  |   BIGDECIMAL  |   false
        "TEST"  |   BIGINTEGER  |   false
        "TEST"  |   DOUBLE      |   false
        "TEST"  |   FLOAT       |   false
        "TEST"  |   INTEGER     |   false
        "TEST"  |   LONG        |   false
        "TEST"  |   NUMBER      |   false
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
        GralidationResult result0 = EACH.control.call("aDummyList", foo1.aDummyList, nullConstraints)
        then:
        result0.isValid

        when:
        GralidationResult result1 = EACH.control.call("aDummyList", foo1.aDummyList, emptyConstraints)
        then:
        result1.isValid

        when:
        GralidationResult result2 = EACH.control.call("aDummyList", foo1.aDummyList, constraints)
        then:
        result2.isValid

        when:
        GralidationResult result3 = EACH.control.call("aDummyList", foo2.aDummyList, emptyConstraints)
        then:
        result3.isValid

        when:
        GralidationResult result4 = EACH.control.call("aDummyList", foo2.aDummyList, constraints)
        then:
        !result4.isValid
        result4.errors.size() == 1

        when:
        GralidationResult result5 = EACH.control.call("aDummyList", foo3.aDummyList, constraints)
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
        EACHKEY.control.call("dailyHours", foo1.dailyHours, [inlist:daysOfTheWeek]).isValid
        !EACHKEY.control.call("dailyHours", foo2.dailyHours, [inlist:daysOfTheWeek]).isValid
    }

    def "error message has the right format for a simple control"(){
        given:
        DummyObject foo = new DummyObject(name: null)

        when:
        ControlResult controlResult = NULLABLE.control.call("name", foo.name, false)

        then:
        !controlResult.isValid
        controlResult.errorData == [propertyName:"name", errorCode:"gralidation.error.nullable", value:null, expected:false]
    }

    def "error message has the right format for a multiple control"(){
        given:
        DummyObject foo = new DummyObject(aDummyList: ["BATMAN", "SUPERMAN", "Super French Man", "LOIC THE SUPER HERO FLYING THROUGH THE SKY"])

        Map constraints = [maxsize:10]

        when:
        GralidationResult result = EACH.control.call("aDummyList", foo.aDummyList, constraints)

        then:
        !result.isValid
        result.errors.size() == 2
        result.errors[0] == [propertyName:"aDummyList", errorCode:"gralidation.error.maxsize", value:"Super French Man", expected:10]
        result.errors[1] == [propertyName:"aDummyList", errorCode:"gralidation.error.maxsize", value:"LOIC THE SUPER HERO FLYING THROUGH THE SKY", expected:10]
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