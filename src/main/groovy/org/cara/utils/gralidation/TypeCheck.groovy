package org.cara.utils.gralidation

enum TypeCheck {

    BIGDECIMAL({String value ->value.isBigDecimal()}),
    BIGINTEGER({String value ->value.isBigInteger()}),
    DOUBLE({String value ->value.isDouble()}),
    FLOAT({String value ->value.isFloat()}),
    INTEGER({String value -> value.isInteger()}),
    LONG({String value ->value.isLong()}),
    NUMBER({String value ->value.isNumber()})

    Closure check

    TypeCheck(Closure check){
        this.check = check
    }
}