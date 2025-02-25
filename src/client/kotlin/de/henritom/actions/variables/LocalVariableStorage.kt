package de.henritom.actions.variables

import de.henritom.actions.util.MessageUtil

class LocalVariableStorage {

    val variables = mutableMapOf<String, Any>()

    fun setVariable(name: String, value: Any) {
        variables[name] = if (value is String) MessageUtil(null).translateVariables(value) else value
    }

    fun removeVariable(name: String) {
        variables.remove(name)
    }

    fun getVariable(name: String): Any? {
        return variables[name]
    }
}