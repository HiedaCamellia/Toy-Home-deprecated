package org.hiedacamellia.toyhome.register;

import com.mojang.logging.LogUtils;
import net.minecraftforge.registries.DeferredRegister;
import org.slf4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class Utils {
    private static final Logger LOGGER = LogUtils.getLogger();

    public static <T> Map<Field, T> getStaticFinalFieldsAndValue(Class<?> clazz, Class<?> valueType) {
        Map<Field, T> staticFinalFieldsAndValue = new HashMap<>();
        try {
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                int modifiers = field.getModifiers();
                if (Modifier.isStatic(modifiers) && Modifier.isFinal(modifiers) && field.getType() == valueType) {
                    boolean accessible = field.canAccess(null);
                    field.setAccessible(true);
                    T value = (T) field.get(null);
                    field.setAccessible(accessible);

                    staticFinalFieldsAndValue.put(field, value);
                }
            }
        } catch (IllegalAccessException e) {
            LOGGER.error("Failed to access field", e);
        }
        return staticFinalFieldsAndValue;
    }

    public static <T> List<AutoRegistryObject<T>> registryModule(Class<?> moduleClass, DeferredRegister<T> register) {
        Map<Field, AutoRegistryObject<T>> registryObjects = getStaticFinalFieldsAndValue(moduleClass, AutoRegistryObject.class);
        List<String> registryIds = registryObjects.keySet().stream().map(Field::getName).map(String::toLowerCase).toList();
        List<AutoRegistryObject<T>> registryObjectsList = registryObjects.values().stream().toList();
        return IntStream.range(0, registryObjects.size()).mapToObj(i -> List.of(registryIds.get(i), registryObjectsList.get(i))).map(o -> {
            String id = (String) o.get(0);
            AutoRegistryObject<T> registryObject = (AutoRegistryObject<T>) o.get(1);
            registryObject.register(register, id);
            return registryObject;
        }).toList();
    }
}

