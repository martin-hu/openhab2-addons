package org.openhab.binding.luxtronik1.internal.lux;

public class CommandFactory {

    public static String createCommand(String key, String[] values) {
        return key + (values.length > 0 ? Lux1Constants.FIELD_DELIM + values.length + Lux1Constants.FIELD_DELIM + String.join(Lux1Constants.FIELD_DELIM, values) : "");
    }
}
