package de.henritom.actions.variables

class LocalVariableStorage {

    private val variables = mutableMapOf<String, Any>()

    fun addVariable(name: String, value: Any) {
        variables[name] = value
    }

    fun removeVariable(name: String) {
        variables.remove(name)
    }

    fun getVariable(name: String): Any? {
        return variables[name]
    }
}