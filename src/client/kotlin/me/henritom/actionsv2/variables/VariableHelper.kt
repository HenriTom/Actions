package me.henritom.actionsv2.variables

import me.henritom.actionsv2.axn.AxnContext

object VariableHelper {

    fun replaceStr(str: String, context: AxnContext? = null): String {
        var new = str

        for (word in str.split(" "))
            if (word.startsWith("$"))
                new = new.replaceFirst(word, get(word.substring(1)).toString())

        return new
    }

    fun get(variable: String, context: AxnContext? = null): Any? {
        if (variable.startsWith("$"))
            return variable

        if (variable.startsWith("local."))
            return LocalVariableStorage.getVariable(variable.substring("local.".length))

        if (variable.startsWith("global."))
            return GlobalVariableStorage.getVariable(variable.substring("global.".length))

        if (context != null && context.variables[variable] != null)
            return context.variables[variable]

        if (LocalVariableStorage.getVariable(variable) != null)
            return LocalVariableStorage.getVariable(variable)

        return GlobalVariableStorage.getVariable(variable)
    }
}