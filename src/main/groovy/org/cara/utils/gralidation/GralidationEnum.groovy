package org.cara.utils.gralidation

enum GralidationEnum {
    NULLABLE("nullable","gralidation.nullable",{parameterToControl, isNullable ->
        return isNullable || (!isNullable && parameterToControl!=null)
    }),
    MAXLENGTH("maxlength","gralidation.maxlength",{parameterToControl, maxlength ->
        return parameterToControl.size()<=maxlength
    }),
    MINLENGTH("minlength","gralidation.minlength",{parameterToControl, minlength ->
        return parameterToControl.size()>=minlength
    })

    String value
    String errorCode
    Closure control

    GralidationEnum(String value, String errorCode, Closure control){
        this.value = value
        this.errorCode = errorCode
        this.control = control
    }
}