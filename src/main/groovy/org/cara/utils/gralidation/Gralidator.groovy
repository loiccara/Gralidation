package org.cara.utils.gralidation

/**
 * Created by loic on 24/01/2016.
 */
class Gralidator {

    static def gralidate(Object object){
        // get constraints then process all constraints

        println object.properties
        Map objectConstraints = object.constraints

        if (objectConstraints == null){
            throw new Exception()
        }

        object.properties.each {
            println it

        }
/*
        objectConstraints.each {
            // read the property
            String propertyName = it.key
            Map propertyConstraints = it.value
            object.getProperties().get(propertyName)
            // check all constraints for this property

        }
        */
    }

}
