# Incore

Incore is a library plugin that I've slowly built up over my time bouncing around between various Minecraft servers and networks. It contains some of my own utility classes and heavily-edited utility classes that I've just picked up.


## Incore's Features

 - `ItemBuilder`, a class made to streamline the creation of items in a way very similar to the `StringBuilder` class.
 - `Formatter.java`, which is basically a giant ball of string-related methods that are loosely connected.
 - Modules, which are basically cute little mini-plugins. They have their own listeners and configuration files, too.
 - `IPlugin`, my own extension of `JavaPlugin` with a few more convenient methods.
 - sk89q's command framework that is used in WorldEdit and WorldGuard. It's awesome.

## Compiling Incore

Incore is built with Maven. You can compile it with `mvn clean install`.
