package com.drake.droidblox.backend.activitywatcher.models

class LogEntries {
    companion object {
        val GameJoiningEntry = "[FLog::Output] ! Joining game"
        val GameMessageEntry = "[FLog::Output] [BloxstrapRPC]"
        val GameTeleportingEntry = "[FLog::GameJoinUtil] GameJoinUtil::initiateTeleportToPlace"
        val GameJoiningPrivateServerEntry = "[FLog::GameJoinUtil] GameJoinUtil::joinGamePostPrivateServer"
        val GameJoiningReservedServerEntry = "[FLog::GameJoinUtil] GameJoinUtil::initiateTeleportToReservedServer"
        val GameJoiningUniverseEntry = "[FLog::GameJoinLoadTime] Report game_join_loadtime:"
        val GameJoiningUDMUXEntry = "[FLog::Network] UDMUX Address = "
        val GameJoinedEntry = "[FLog::Network] serverId:"
        val GameDisconnectedEntry = "[FLog::Network] Time to disconnect replication data:"
        val GameLeavingEntry = "[FLog::SingleSurfaceApp] leaveUGCGameInternal"
        val RobloxExitedEntry = "[FLog::SingleSurfaceApp] destroyLuaApp"

        val GameJoiningEntryPattern = Regex("""! Joining game '([0-9a-f\-]{36})' place ([0-9]+) at ([0-9\.]+)""")
        val GameJoiningPrivateServerPattern = Regex("""accessCode"":""([0-9a-f\-]{36})""")
        val GameJoiningUniversePattern = Regex("""userid:([0-9]+), .*universeid:([0-9]+)""")
        val GameJoiningUDMUXPattern = Regex("""UDMUX Address = ([0-9\.]+)""")
        val GameJoinedEntryPattern = Regex("""serverId: ([0-9\.]+)\|[0-9]+""")
        val GameMessageEntryPattern = Regex("""\[BloxstrapRPC\] (.*)""")
    }
}