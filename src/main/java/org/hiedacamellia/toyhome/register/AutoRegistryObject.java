package org.hiedacamellia.toyhome.register;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class AutoRegistryObject<T> {
    private final Supplier<T> supplier;
    private RegistryObject<T> object;

    public AutoRegistryObject(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> AutoRegistryObject<T> of(Supplier<T> supplier) {
        return new AutoRegistryObject<>(supplier);
    }

    public void register(DeferredRegister<T> register, String name) {
        object = register.register(name, supplier);
    }

    public T get() {
        return object.get();
    }

    public Supplier<T> getSupplier() {
        return supplier;
    }
}
