### Features:
1. Authme bot support
2. ~~https://github.com/LeavesMC/Leaves/issues/281~~ (Fixed on server side)
3. https://github.com/LeavesMC/Leaves/issues/282
4. ~~https://github.com/LeavesMC/Leaves/issues/329~~ (Fixed on server side)
5. ~~https://github.com/LeavesMC/Leaves/issues/356~~ (Fixed on server side)
6. ~~https://github.com/LeavesMC/Leaves/issues/411~~ (Fixed on server side)
7. https://github.com/LeavesMC/Leaves/issues/434
8. Bot skin inherited (bot's skin will be default to the creator)

### Config
Config is in the root directory in your server's folder (should be with your leaves-version.jar)
```yaml
feature:
  # Players can place wither rose everywhere without triggering block update
  better-wither-rose-place: false

  # The fakeplayer creation limit is able to set by player use bukkit permission system
  # Use bukkit.command.bot.x to give a player x max fakeplayer limit
  # Use bukkit.command.bot.unlimited to disable limit of a player
  # If enabled, the limit from leaves.yml will be ignored
  better-bot-creation: false

  # The fakeplayer will inherit creator's skin instead of default skin by its name
  bot-skin-inherit: false
fix:
  # Let leaves' fakeplayer work correctly with Authme plugin
  authme-bot-support: true
```