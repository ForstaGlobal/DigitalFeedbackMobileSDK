package com.confirmit.testsdkapp.viewmodels.models

import androidx.annotation.Keep
import com.confirmit.mobilesdk.database.externals.Program
import com.confirmit.testsdkapp.ProgramConfig
import java.io.Serializable

@Keep
class ProgramModel(program: TriggerProgram) : Serializable {
    var program: TriggerProgram = program
        private set
    var configs: ProgramConfig
        private set

    constructor(program: Program) : this(TriggerProgram(program))

    init {
        this.configs = ProgramConfig(program.serverId, program.programKey)
    }

    fun update(program: Program) {
        this.program = TriggerProgram(program)
    }

    fun getCustomData(): Map<String, String> {
        val values = mutableMapOf<String, String>()
        val raw = configs.customData
        raw.split(";").forEach {
            val keyValue = it.split("=")
            if (keyValue.size == 2) {
                val key = keyValue[0]
                val value = keyValue[1]
                values[key] = value
            }
        }
        return values
    }
}

@Keep
class TriggerProgram(program: Program) : Serializable {
    val serverId: String = program.serverId
    val programKey: String = program.programKey
    val started: Boolean = program.started
    val publishedVersion: Long = program.publishedVersion
}