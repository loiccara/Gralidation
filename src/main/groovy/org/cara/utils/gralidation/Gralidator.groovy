package org.cara.utils.gralidation

/**
 * Created by loic on 24/01/2016.
 */
class Gralidator {

    static Set is

    static def initGralidator(){
        Object.metaClass.gralidate = {
            return gralidate(delegate)
        }
        Object.metaClass.errors = []
    }

    static def gralidate(Object object){
        boolean result = true
        List<String> issues = []

        Map objectConstraints = object.constraints

        if (objectConstraints == null){
            throw new MissingPropertyException("No constraints specified for ${object.class}")
        }

        objectConstraints.each {
            String propertyName = it.key
            def propertyValue = object.getProperties().get(propertyName)

            Map propertyConstraints = it.value
            propertyConstraints.each {
                def constraint = it.key.toUpperCase()
                def controlValue = it.value

                GralidationEnum currentControl = constraint as GralidationEnum
                if (!currentControl.control.call(propertyValue, controlValue)){
                    issues.add(currentControl.errorCode)
                }
            }
        }

        result=issues.size()==0
        object.errors = issues

        return result
    }
}
