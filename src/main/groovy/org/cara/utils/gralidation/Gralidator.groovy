package org.cara.utils.gralidation

import org.apache.commons.lang3.ClassUtils

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
            GralidationResult tempResult = executeControls(propertyValue, controls)
            errors.addAll(tempResult.errors)

            if (propertyValue && isEmbeddedComplexObject(object, propertyName)){
                // gralidate the embedded object
                GralidationResult embeddedResult = gralidate(propertyValue)
                errors.addAll(embeddedResult.errors)
            }
        }
        new GralidationResult(isValid:errors.isEmpty(), errors:errors)
    }

    static GralidationResult gralidate(Map myMap, Map constraints, boolean validateInexistantKeys){
        List<String> errors = []
        constraints.each { String propertyName, Map controls ->
            if (myMap?.containsKey(propertyName)){
                def propertyValue = myMap.get(propertyName)
                GralidationResult tempResult = executeControls(propertyValue, controls)
                errors.addAll(tempResult.errors)
            } else if(validateInexistantKeys) {
                errors.add(ERROR_CODE_PREFIX + "inexistantProperty:" + propertyName)
            }
        }
        new GralidationResult(isValid:errors.isEmpty(), errors:errors)
    }

    protected static GralidationResult controlList(List myList, Map controls){
        List errors = []
        myList.each {
            GralidationResult thisGralidation = executeControls(it, controls)
            errors.addAll(thisGralidation.errors)
        }
        new GralidationResult(isValid:errors.isEmpty(), errors:errors)
    }

    private static GralidationResult executeControls(def propertyValue, Map controls){
        List errors = []
        controls.each {String constraint, def controlValue ->
            GralidationEnum currentControl = constraint.toUpperCase() as GralidationEnum
            GralidationResult thisResult = currentControl.control.call(propertyValue, controlValue)
            if (!thisResult.isValid) {
                errors.addAll(thisResult.errors)
            }
        }
        new GralidationResult(isValid:errors.isEmpty(), errors:errors)
    }

    private static boolean isEmbeddedComplexObject(Object object, String propertyName){
        Class propertyClass = object.properties.get(propertyName).class
        !ClassUtils.isPrimitiveOrWrapper(propertyClass) && !(propertyClass in NON_EMBEDDED_OBJECTS)
    }

    private static final Set<Class> NON_EMBEDDED_OBJECTS = [
            List.class,
            Set.class,
            Map.class,
            String.class,
            ArrayList.class
    ]
}
