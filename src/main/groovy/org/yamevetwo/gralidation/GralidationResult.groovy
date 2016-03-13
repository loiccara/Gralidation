package org.yamevetwo.gralidation

import jdk.nashorn.internal.ir.annotations.Immutable

@Immutable
class GralidationResult {
    boolean isValid
    List<String> errors
}
