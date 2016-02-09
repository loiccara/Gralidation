package org.cara.utils.gralidation

import jdk.nashorn.internal.ir.annotations.Immutable

@Immutable
class GralidationResult {
    boolean isValid
    List<String> errors
}
