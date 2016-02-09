package org.cara.utils.gralidation

import sun.reflect.generics.reflectiveObjects.NotImplementedException

enum GralidationEnum {

    BLANK("blank", "blank", {def parameterToControl, boolean isBlankable ->
        isBlankable || (!isBlankable && parameterToControl!="")
    }),
    EMAIL("email", "email", {def parameterToControl, boolean isEmailExpected ->
        throw new NotImplementedException()
    }),
    INLIST("inlist", "inlist", {def parameterToControl, List allowedValues ->
        throw new NotImplementedException()
    }),
    MATCHES("matches", "matches", {def parameterToControl, String regexp->
        throw new NotImplementedException()
    }),
    MAX("max", "max", {def parameterToControl, def max ->
        throw new NotImplementedException()
    }),
    MAXSIZE("maxlength","maxsize",{def parameterToControl, int maxsize ->
        parameterToControl?.size()<=maxsize
    }),
    MIN("min", "min", {def parameterToControl, def min ->
        throw new NotImplementedException()
    }),
    MINSIZE("minlength","minsize",{def parameterToControl, int minsize ->
        parameterToControl?.size()>=minsize
    }),
    NOTEQUAL("notequal", "notequal", {def parameterToControl, def value ->
        parameterToControl!=value
    }),
    NULLABLE("nullable","nullable",{def parameterToControl, boolean isNullable ->
        isNullable || (!isNullable && parameterToControl!=null)
    }),
    RANGE("range", "notequal", {def parameterToControl, def range ->
        throw new NotImplementedException()
    }),
    URL("url", "url", {def parameterToControl, def urlExpected ->
        throw new NotImplementedException()
    })

    final String value
    final String errorCode
    final Closure control

    GralidationEnum(String value, String errorCode, Closure control){
        this.value = value
        this.errorCode = errorCode
        this.control = control
    }
}