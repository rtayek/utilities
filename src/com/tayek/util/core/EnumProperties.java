package com.tayek.util.core;
import java.util.Properties;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
public final class EnumProperties {
    private EnumProperties() {}
    public static <E extends Enum<E>> Properties toProperties(E[] values,Function<E,?> valueMapper) {
        Properties properties=new Properties();
        putInto(properties,values,valueMapper);
        return properties;
    }
    public static <E extends Enum<E>> void putInto(Properties properties,E[] values,Function<E,?> valueMapper) {
        for(E value:values) {
            Object mapped=valueMapper.apply(value);
            if(mapped!=null) properties.setProperty(value.name(),mapped.toString());
        }
    }
    public static <E extends Enum<E>> void apply(Properties properties,E[] values,BiConsumer<E,String> setter) {
        apply(properties,values,setter,null);
    }
    public static <E extends Enum<E>> void apply(Properties properties,E[] values,BiConsumer<E,String> setter,Consumer<E> onMissing) {
        for(E value:values) {
            String property=properties.getProperty(value.name());
            if(property!=null) setter.accept(value,property);
            else if(onMissing!=null) onMissing.accept(value);
        }
    }
}
