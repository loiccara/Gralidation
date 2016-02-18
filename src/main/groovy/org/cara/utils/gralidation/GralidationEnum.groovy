package org.cara.utils.gralidation

import sun.reflect.generics.reflectiveObjects.NotImplementedException

import static org.cara.utils.gralidation.Gralidator.ERROR_CODE_PREFIX

enum GralidationEnum {

    BLANK("blank", {def propertyName, def parameterToControl, boolean isBlankable ->
        boolean result = isBlankable || parameterToControl==null || (!isBlankable && parameterToControl?.trim())
        new GralidationResult(isValid:result, errors:result?[]:[getErrorMessage("blank", propertyName, parameterToControl, isBlankable)])
    }),
    EMAIL("email", {def propertyName, def parameterToControl, boolean isEmailExpected ->
        throw new NotImplementedException()
    }),
    INLIST("inlist", {def propertyName, def parameterToControl, List allowedValues ->
        boolean result = parameterToControl in allowedValues
        new GralidationResult(isValid:result, errors:result?[]:[getErrorMessage("inlist", propertyName, parameterToControl, allowedValues)])
    }),
    MATCHES("matches", {def propertyName, def parameterToControl, String regexp->
        throw new NotImplementedException()
    }),
    MAX("max", {def propertyName, def parameterToControl, def max ->
        boolean result = parameterToControl <= max
        new GralidationResult(isValid:result, errors:result?[]:[getErrorMessage("max", propertyName, parameterToControl, max)])
    }),
    MAXSIZE("maxsize",{def propertyName, def parameterToControl, int maxsize ->
        boolean result = parameterToControl?.size()<=maxsize
        new GralidationResult(isValid:result, errors:result?[]:[getErrorMessage("maxsize", propertyName, parameterToControl, maxsize)])
    }),
    MIN("min", {def propertyName, def parameterToControl, def min ->
        boolean result = parameterToControl > min
        new GralidationResult(isValid:result, errors:result?[]:[getErrorMessage("min", propertyName, parameterToControl, min)])
    }),
    MINSIZE("minsize",{def propertyName, def parameterToControl, int minsize ->
        boolean result = parameterToControl?.size()>=minsize
        new GralidationResult(isValid:result, errors:result?[]:[getErrorMessage("minsize", propertyName, parameterToControl, minsize)])
    }),
    NOTEQUAL("notequal", {def propertyName, def parameterToControl, def value ->
        boolean result = parameterToControl!=value
        new GralidationResult(isValid:result, errors:result?[]:[getErrorMessage("notequal", propertyName, parameterToControl, value)])
    }),
    NULLABLE("nullable",{def propertyName, def parameterToControl, boolean isNullable ->
        boolean result = isNullable || (!isNullable && parameterToControl!=null)
        new GralidationResult(isValid:result, errors:result?[]:[getErrorMessage("nullable", propertyName, parameterToControl, isNullable)])
    }),
    RANGE("range", {def propertyName, def parameterToControl, def range ->
        throw new NotImplementedException()
    }),
    TYPE("type", {def propertyName, def parameterToControl, def className ->
        parameterToControl
    }),
    URL("url", {def propertyName, def parameterToControl, def urlExpected ->
        throw new NotImplementedException()
    }),
    EACH("each", {def propertyName, List parameterToControl, Map controls ->
        Gralidator.controlList(propertyName, parameterToControl, controls)
    }),
    EACHKEY("eachkey", {def propertyName, Map parameterToControl, Map controls ->
        Gralidator.controlList(propertyName, parameterToControl.keySet().toList(), controls)
    })

    final String value
    final Closure control

    GralidationEnum(String value, Closure control){
        this.value = value
        this.control = control
    }

    private static String getErrorMessage(String errorCode, def propertyName, def parameterToControl, def control){
        "[propertyName:${propertyName},value:${parameterToControl},errorCode:${ERROR_CODE_PREFIX + errorCode},control:${control}]"
    }
}