package org.yamevetwo.gralidation

import groovy.transform.Immutable

@Immutable
class ControlResult {
    boolean isValid
    Map errorData
}
