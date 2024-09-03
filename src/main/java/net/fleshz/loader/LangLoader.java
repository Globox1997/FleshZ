package net.fleshz.loader;

import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;
import net.fleshz.init.BlockInit;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;

import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;

public class LangLoader extends FabricLanguageProvider {

    public LangLoader(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
        super(dataOutput, registryLookup);
    }

    @Override
    public void generateTranslations(RegistryWrapper.WrapperLookup registryLookup, TranslationBuilder translationBuilder) {
        for (Block entry : BlockInit.RACKS.keySet()) {

            String[] translationParts = entry.getTranslationKey().split("\\.");
            String[] words = translationParts[translationParts.length - 1].split("_");

            StringBuilder result = new StringBuilder();
            for (String word : words) {
                if (!word.isEmpty()) {
                    result.append(Character.toUpperCase(word.charAt(0))).append(word.substring(1)).append(" ");
                }
            }
            translationBuilder.add(entry, result.toString().trim());
        }
        try {
            Path existingFilePath = dataOutput.getModContainer().findPath("assets/fleshz/lang/en_us.json").get();
            translationBuilder.add(existingFilePath);

        } catch (Exception e) {
            throw new RuntimeException("Failed to add existing language file!", e);
        }
    }
}
