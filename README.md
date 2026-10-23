# FleshZ
FleshZ adds a drying rack to have another way to obtain leather.

### Installation
FleshZ is a mod built for the [Fabric Loader](https://fabricmc.net/). It requires [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api) to be installed separately; all other dependencies are installed with the mod.

### License
FleshZ is licensed under MIT.

### Datapacks
Add your own drying rack recipes with a datapack!
Put your json files under `data/<namespace>/recipe/`.
Each file creates its own recipe for the drying rack.
Inside the file put for example this:
```
{
    "type": "fleshz:rack_drying",
    "ingredient": {
        "item": "minecraft:kelp"
    },
    "result": {
        "id": "minecraft:dried_kelp"
    },
    "time": 2400
}
```
"ingredient" represents the item you will hang on, a tag works too (`"tag": "c:hides"`)  
"result" will be the resulting item  
"time" is the base time in ticks (1 second = 20 ticks)

The old `rack_items` folder is no longer supported.
