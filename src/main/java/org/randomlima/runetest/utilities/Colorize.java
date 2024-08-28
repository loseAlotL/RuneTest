package org.randomlima.runetest.utilities;

import org.bukkit.ChatColor;

public class Colorize {
    public static String format(String str){
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}
/*
Dark Red: 4
Red: c
Gold: 6
Yellow: e
Dark Green: 2
Green: a
Aqua: b
Dark Aqua: 3
Dark Blue: 1
Blue: 9
Light Purple: d
Dark Purple: 5
White: f
Gray: 7
Dark Gray: 8
Black: 0

Obfuscated: k
Bol: l
Strikethrough: m
Underline: n
Italic: o

Reset: r
*/