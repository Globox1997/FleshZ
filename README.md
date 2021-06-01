# FleshZ
FleshZ adds a drying rack to have another way to obtain leather.

### Installation
FleshZ is a mod built for the [Fabric Loader](https://fabricmc.net/). It requires [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) to be installed separately; all other dependencies are installed with the mod.

### License
FleshZ is licensed under MIT.


Add your own drying rack recipes with a datapack!
Put under data/rotten/rack_items your json files.
Each file creates its own recipe for the drying rack.
Inside the file put for example this:
```
{
    "item": "minecraft:kelp",
    "result": "minecraft:dried_kelp",
    "time": 4800
}
```
"item" represents the item you will hang on

"result" will be the resulting item

"time" is the time in ticks (1 second = 20 ticks)
