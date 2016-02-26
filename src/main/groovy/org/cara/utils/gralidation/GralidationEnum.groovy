package org.cara.utils.gralidation

import org.apache.commons.validator.routines.EmailValidator
import sun.reflect.generics.reflectiveObjects.NotImplementedException

import java.util.regex.Pattern

import static org.cara.utils.gralidation.Gralidator.ERROR_CODE_PREFIX

enum GralidationEnum {

    BLANK("blank", false, {def propertyName, def parameterToControl, boolean isBlankable ->
        boolean result = isBlankable || parameterToControl==null || (!isBlankable && parameterToControl?.trim())
        new ControlResult(isValid:result, errorData:result?[:]:getError("blank", propertyName, parameterToControl, isBlankable))
    }),
    EMAIL("email", false, {def propertyName, def parameterToControl, boolean isEmailExpected ->
        boolean isEmail = EmailValidator.getInstance(true).isValid(parameterToControl)
        boolean result = (isEmailExpected && isEmail) || (!isEmailExpected && !isEmail)
        new ControlResult(isValid:result, errorData:result?[:]:getError("email", propertyName, parameterToControl, isEmailExpected))
    }),
    INLIST("inlist", false, {def propertyName, def parameterToControl, List allowedValues ->
        boolean result = parameterToControl in allowedValues
        new ControlResult(isValid:result, errorData:result?[:]:getError("inlist", propertyName, parameterToControl, allowedValues))
    }),
    MATCHES("matches", false, { def propertyName, def parameterToControl, Pattern pattern->
        boolean result = (parameterToControl ==~ pattern)
        new ControlResult(isValid:result, errorData:result?[:]:getError("matches", propertyName, parameterToControl, pattern))
    }),
    MAX("max", false, {def propertyName, def parameterToControl, def max ->
        boolean result = parameterToControl <= max
        new ControlResult(isValid:result, errorData:result?[:]:getError("max", propertyName, parameterToControl, max))
    }),
    MAXSIZE("maxsize", false, {def propertyName, def parameterToControl, int maxsize ->
        boolean result = parameterToControl?.size()<=maxsize
        new ControlResult(isValid:result, errorData:result?[:]:getError("maxsize", propertyName, parameterToControl, maxsize))
    }),
    MIN("min", false, {def propertyName, def parameterToControl, def min ->
        boolean result = parameterToControl > min
        new ControlResult(isValid:result, errorData:result?[:]:getError("min", propertyName, parameterToControl, min))
    }),
    MINSIZE("minsize", false, {def propertyName, def parameterToControl, int minsize ->
        boolean result = parameterToControl?.size()>=minsize
        new ControlResult(isValid:result, errorData:result?[:]:getError("minsize", propertyName, parameterToControl, minsize))
    }),
    NOTEQUAL("notequal", false, {def propertyName, def parameterToControl, def value ->
        boolean result = parameterToControl!=value
        new ControlResult(isValid:result, errorData:result?[:]:getError("notequal", propertyName, parameterToControl, value))
    }),
    NULLABLE("nullable", false, {def propertyName, def parameterToControl, boolean isNullable ->
        boolean result = isNullable || (!isNullable && parameterToControl!=null)
        new ControlResult(isValid:result, errorData:result?[:]:getError("nullable", propertyName, parameterToControl, isNullable))
    }),
    RANGE("range", false, {def propertyName, def parameterToControl, def range ->
        throw new NotImplementedException()
    }),
    TYPE("type", false, {def propertyName, def parameterToControl, TypeCheck typeCheck ->
        boolean result = typeCheck.check.call(parameterToControl)
        new ControlResult(isValid:result, errorData:result?[:]:getError("type", propertyName, parameterToControl, typeCheck))
    }),
    URL("url", false, {def propertyName, def parameterToControl, def urlExpected ->
        throw new NotImplementedException()
    }),
    EACH("each", true, {def propertyName, List parameterToControl, Map controls ->
        Gralidator.controlList(propertyName, parameterToControl, controls)
    }),
    EACHKEY("eachkey", true, {def propertyName, Map parameterToControl, Map controls ->
        Gralidator.controlList(propertyName, parameterToControl.keySet().toList(), controls)
    })

    final String value
    final boolean isMultipleControl
    final Closure control

    GralidationEnum(String value, isMultipleControl, Closure control){
        this.value = value
        this.isMultipleControl = isMultipleControl
        this.control = control
    }

    private static Map getError(String errorCode, def propertyName, def value, def expected){
        ["propertyName":propertyName, "errorCode":(Gralidator.ERROR_CODE_PREFIX + errorCode), "value":value, "expected":expected]
    }
}