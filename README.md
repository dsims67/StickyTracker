# StickyTracker
StickyTracker by savior67 - 
License: 
MIT 

Requirements:
Spigot v1.9.2

Description:
This plugin adds a configurable tracker item. Trackers can be stuck to players by hitting them with the item. When a tracker is attached to a player a particle trail will be displayed on the attaching player's screen leading to the target.

Sticky Tracker
This plugin allows a configurable tracker item to be used to track other players. Once a player holding the tracker hits another player with it, it will leave their inventory and a particle trail is displayed pointing to the player hit with the tracker. The particle trail can be hidden by right clicking with the tracker item in hand. A player being tracked can remove the tracker by taking fire damage or dying. A small explosion can be heard if a tracker attached to you has been destroyed.

Admin Commands
/strack track (player) <target> - places a tracker on the target player
/strack clear - removes your trackers
/strack list - lists all of the trackers attached to players
  
Permissions
stickytracker.admin - gives player access to all admin commands listed above
stickytracker.use - allows player to stick trackers on others
stickytracker.untrackable - prevents the player from being tracked (can be bypassed by track command)

Configuration
tracker-item: 151 - the tracker item, daylight sensor by default
fire-destroy-enabled: true - allows fire damage to destroy trackers attached to players
