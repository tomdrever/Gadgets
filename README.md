# Gadgets #

**Updated to 1.18.2!**

- Introduced ability to combine gadgets via crafting
- Reduced default number of gadgets to 2 to encourage but not enforce combining gadgets
- Updated item textures and introduced new biome icons thanks to SirDurpsalot

![Gadget items - compass, depthmeter and biometer]( https://i.imgur.com/UIZ3qTX.png "Gadget items - the clock, combined gadget, biometer, personal compass and depthmeter")

***

Adds items that can be equipped (using the [Curios API](https://www.curseforge.com/minecraft/mc-mods/curios)) to show the player's position and biome, to replace the F3 debug info. 
Items in curios slots can be rearranged to your liking, and the number of available gadget slots can be changed in config.

![Gadget gui](https://imgur.com/90Bvzfu.jpg "Gadget GUI display")

Current Gadget items:

- Biometer: shows current biome
- Timepiece: shows current time and day
- Personal Compass: shows the direction the player is facing and their position (X, Z)
- Depthmeter: shows the player's current Y level
- (If [Serene Seasons](https://www.curseforge.com/minecraft/mc-mods/serene-seasons) installed) Personal Calendar: shows the current season and sub-season

***

Config options also let you change the position of the gadget gui, and whether you need to have the gadgets equipped.

The biome icons are taken and adapted from [this file](https://gamepedia.cursecdn.com/minecraft_gamepedia/5/59/BiomeCSS.png) from the Minecraft Wiki.

Supports custom biomes - just put an 18*18 icon in your mod/resource pack in `assets/gadgets/textures/gui/biome_icons/MOD_ID/BIOME_NAME.png`. 
The mod comes with icons for the biomes from: 

- [Abundance](https://www.curseforge.com/minecraft/mc-mods/abundance)
- [Atmospheric](https://www.curseforge.com/minecraft/mc-mods/atmospheric)
- [Autumnity](https://www.curseforge.com/minecraft/mc-mods/autumnity)
- [Bayou Blues](https://www.curseforge.com/minecraft/mc-mods/bayou-blues)
- [The Endergetic Expansion](https://www.curseforge.com/minecraft/mc-mods/endergetic)
- [Environmental](https://www.curseforge.com/minecraft/mc-mods/environmental)
- [Infernal Expansion](https://www.curseforge.com/minecraft/mc-mods/infernal-expansion)
- [The Outer End](https://www.curseforge.com/minecraft/mc-mods/the-outer-end) (icons by SirDurpsalot)
- [Terrestria Reforged](https://www.curseforge.com/minecraft/mc-mods/terrestria-reforged) (icons by SirDurpsalot) (WIP)


*NOTE: if an update adds a new gadget, you may want to increase the config option for number of curio slots, as the default is the number of gadgets and the file won't update between versions.*

***

Feedback and suggestions welcome.