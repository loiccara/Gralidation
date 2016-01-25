package org.cara.utils.gralidation

enum GralidationEnum {
    NULLABLE("nullable","gralidation.error.nullable",{def parameterToControl, boolean isNullable ->
        return isNullable || (!isNullable && parameterToControl!=null)
    }),
    MAXLENGTH("maxlength","gralidation.error.maxlength",{def parameterToControl, int maxlength ->
        return parameterToControl?.size()<=maxlength
    }),
    MINLENGTH("minlength","gralidation.error.minlength",{def parameterToControl, int minlength ->
        return parameterToControl?.size()>=minlength
    }),
    BLANK("blank", "gralidation.error.blank", {def parameterToControl, boolean isBlankable ->
        return isBlankable || (!isBlankable && parameterToControl!="")
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