package com.confirmit.testsdkapp.utils

import android.content.Context
import com.confirmit.mobilesdk.ConfirmitSDK
import com.confirmit.mobilesdk.ConfirmitServer
import com.confirmit.mobilesdk.database.externals.Server
import com.confirmit.testsdkapp.MainApplication
import com.google.gson.Gson
import java.util.*

data class ServerPref(val clientId: String = "", val clientSecret: String = "")

object ServerManager {
    private const val HOST_SERVER = "mobilesdkHostServer_"
    private val gson = Gson()

    fun writeServerToPref(serverId: String, serverPref: ServerPref) {
        val sharedPref = (ConfirmitSDK.androidContext as MainApplication).activity?.getPreferences(Context.MODE_PRIVATE)
                ?: return
        with(sharedPref.edit()) {
            val serverJson: String = if (serverPref.clientId.isNotEmpty() && serverPref.clientSecret.isNotEmpty()) {
                gson.toJson(serverPref)
            } else {
                gson.toJson(ServerPref())
            }
            putString(HOST_SERVER + serverId, serverJson)
            commit()
        }
    }

    fun getServerPref(selectedServerPos: Int): ServerPref? {
        val servers = getDefaultServers()
        val sharedPref = (ConfirmitSDK.androidContext as MainApplication).activity?.getPreferences(Context.MODE_PRIVATE)
                ?: return null
        servers[selectedServerPos].let { server ->
            sharedPref.getString(HOST_SERVER + server.serverId, "")?.let { pref ->
                if (pref.isNotEmpty()) {
                    return gson.fromJson(pref, ServerPref::class.java)
                }
            }
        }
        return null
    }

    fun setNewConfiguration(serverId: String, clientId: String, clientSecret: String): Server {
        val servers: List<Server> = ConfirmitServer.getServers()
        val server = servers.first { x -> x.serverId == serverId }

        return ConfirmitServer.configure(UUID.randomUUID().toString(), server.host, clientId, clientSecret)
    }

    fun getDefaultServers(): List<Server> {
        return ConfirmitServer.getServers()
    }
}
