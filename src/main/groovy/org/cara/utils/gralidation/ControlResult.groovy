package org.cara.utils.gralidation

import groovy.transform.Immutable

@Immutable
class ControlResult {
    boolean isValid
    Map errorData
}
