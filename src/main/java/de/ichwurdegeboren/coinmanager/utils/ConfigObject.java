package de.ichwurdegeboren.coinmanager.utils;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ConfigObject {

    private String prefix;
    private String hostname;
    private int port;
    private String database;
    private String username;
    private String password;
    private String tablePrefix;
    private boolean bungeeCommands;
    private boolean spigotCommands;

}
