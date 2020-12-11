package com.example.myapp.domain

import com.example.myapp.ConfigKey
import com.example.myapp.utils.Utils
import java.io.Serializable

class ProgramConfig(val serverId: String, val programKey: String) : Serializable {
    var customData: String
        get() {
            return get(ConfigKey.CUSTOM_DATA, "")
        }
        set(value) {
            set(ConfigKey.CUSTOM_DATA, value)
        }

    private inline fun <reified T> get(key: ConfigKey, def: T): T {
        return Utils.getSetting(createKey(key), def)
    }

    private inline fun <reified T> set(key: ConfigKey, value: T) {
        Utils.setSetting(createKey(key), value)
    }

    private fun createKey(key: ConfigKey): String {
        return "${serverId}_${programKey}_${key.name}"
    }
}