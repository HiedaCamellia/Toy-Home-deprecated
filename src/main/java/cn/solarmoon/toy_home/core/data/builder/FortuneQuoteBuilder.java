package cn.solarmoon.toy_home.core.data.builder;

import cn.solarmoon.toy_home.core.ToyHome;
import cn.solarmoon.toy_home.core.data.serializer.FortuneQuote;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Arrays;
import java.util.Map;

public class FortuneQuoteBuilder extends SimpleJsonResourceReloadListener {

    private static final Gson gson = new GsonBuilder().create();
    private static final String directory = "fortune_quotes";

    public FortuneQuoteBuilder() {
        super(gson, directory);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> map, ResourceManager manager, ProfilerFiller filler) {
        FortuneQuote.QUOTES = new String[] {};
        for(Map.Entry<ResourceLocation, JsonElement> entry : map.entrySet()) {
            FortuneQuote fortuneQuote = gson.fromJson(entry.getValue(), FortuneQuote.class);
            String[] newQuotes = Arrays.copyOf(FortuneQuote.QUOTES, FortuneQuote.QUOTES.length + fortuneQuote.quotes.length);
            System.arraycopy(fortuneQuote.quotes, 0, newQuotes, FortuneQuote.QUOTES.length, fortuneQuote.quotes.length);
            FortuneQuote.QUOTES = newQuotes;
        }
        ToyHome.DEBUG.getLogger().info("{} fortune quotes has been loaded!", FortuneQuote.QUOTES.length);
    }

}
