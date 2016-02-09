package org.cara.utils.gralidation

class Gralidator {

    static final String ERROR_CODE_PREFIX = "gralidation.error."

    static def initGralidator(){
        Object.metaClass.gralidate = {
            return gralidate(delegate)
        }
    }

    static GralidationResult gralidate(Object object){
        List<String> errors = []
        Map objectConstraints = object.constraints

        if (objectConstraints == null){
            throw new MissingPropertyException("No constraints specified for ${object.class}")
        }

        objectConstraints.each { String propertyName, Map controls ->
            def propertyValue = object.getProperties().get(propertyName)
            controls.each { String constraint, def controlValue ->
                GralidationEnum currentControl = constraint.toUpperCase() as GralidationEnum
                if (!currentControl.control.call(propertyValue, controlValue)){
                    errors.add(currentControl.errorCode)
                }
            }
        }

        boolean isValid=errors.size()==0
        new GralidationResult(isValid: isValid, errors:errors)
    }

    static GralidationResult gralidate(Map myMap, Map constraints, boolean validateInexistantKeys){
        List<String> errors = []
        constraints.each { String propertyName, Map controls ->
            if (myMap?.containsKey(propertyName)){
                def propertyValue = myMap.get(propertyName)
                controls.each { String constraint, def controlValue ->
                    GralidationEnum currentControl = constraint.toUpperCase() as GralidationEnum
                    if (!currentControl.control.call(propertyValue, controlValue)) {
                        errors.add(ERROR_CODE_PREFIX + currentControl.errorCode)
                    }
                }
            } else if(validateInexistantKeys) {
                errors.add(ERROR_CODE_PREFIX + "inexistantProperty:" + propertyName)
            }
        }
        boolean isValid=errors.size()==0
        new GralidationResult(isValid: isValid, errors:errors)
    }
}
