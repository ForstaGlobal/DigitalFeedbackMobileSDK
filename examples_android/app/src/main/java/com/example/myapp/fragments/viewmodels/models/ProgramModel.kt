package com.example.myapp.fragments.viewmodels.models

import androidx.annotation.Keep
import com.confirmit.mobilesdk.database.externals.Program
import com.example.myapp.domain.ProgramConfig
import java.io.Serializable

@Keep
class ProgramModel(program: Program) : Serializable {
    var program: Program = program
        private set
    var configs: ProgramConfig
        private set

    init {
        this.configs = ProgramConfig(program.serverId, program.programKey)
    }

    fun update(program: Program) {
        this.program = program
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