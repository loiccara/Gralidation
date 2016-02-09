package org.cara.utils.gralidation

import sun.reflect.generics.reflectiveObjects.NotImplementedException

import static org.cara.utils.gralidation.Gralidator.ERROR_CODE_PREFIX

enum GralidationEnum {

    BLANK("blank", {def parameterToControl, boolean isBlankable ->
        boolean result = isBlankable || (!isBlankable && parameterToControl!="")
        new GralidationResult(isValid:result, errors:result?[]:[ERROR_CODE_PREFIX + "blank"])
    }),
    EMAIL("email", {def parameterToControl, boolean isEmailExpected ->
        throw new NotImplementedException()
    }),
    INLIST("inlist", {def parameterToControl, List allowedValues ->
        boolean result = parameterToControl in allowedValues
        new GralidationResult(isValid:result, errors:result?[]:[ERROR_CODE_PREFIX + "inlist"])
    }),
    MATCHES("matches", {def parameterToControl, String regexp->
        throw new NotImplementedException()
    }),
    MAX("max", {def parameterToControl, def max ->
        boolean result = parameterToControl <= max
        new GralidationResult(isValid:result, errors:result?[]:[ERROR_CODE_PREFIX + "max"])
    }),
    MAXSIZE("maxsize",{def parameterToControl, int maxsize ->
        boolean result = parameterToControl?.size()<=maxsize
        new GralidationResult(isValid:result, errors:result?[]:[ERROR_CODE_PREFIX + "maxsize"])
    }),
    MIN("min", {def parameterToControl, def min ->
        boolean result = parameterToControl > min
        new GralidationResult(isValid:result, errors:result?[]:[ERROR_CODE_PREFIX + "min"])
    }),
    MINSIZE("minsize",{def parameterToControl, int minsize ->
        boolean result = parameterToControl?.size()>=minsize
        new GralidationResult(isValid:result, errors:result?[]:[ERROR_CODE_PREFIX + "minsize"])
    }),
    NOTEQUAL("notequal", {def parameterToControl, def value ->
        boolean result = parameterToControl!=value
        new GralidationResult(isValid:result, errors:result?[]:[ERROR_CODE_PREFIX + "notequal"])
    }),
    NULLABLE("nullable",{def parameterToControl, boolean isNullable ->
        boolean result = isNullable || (!isNullable && parameterToControl!=null)
        new GralidationResult(isValid:result, errors:result?[]:[ERROR_CODE_PREFIX + "nullable"])
    }),
    RANGE("range", {def parameterToControl, def range ->
        throw new NotImplementedException()
    }),
    URL("url", {def parameterToControl, def urlExpected ->
        throw new NotImplementedException()
    }),
    EACH("each", {List parameterToControl, Map controls ->
        Gralidator.controlList(parameterToControl, controls)
    }),
    EACHKEY("eachkey", {Map parameterToControl, Map controls ->
        Gralidator.controlList(parameterToControl.keySet().toList(), controls)
    })

    final String value
    final Closure control

    GralidationEnum(String value, Closure control){
        this.value = value
        this.control = control
    }
}