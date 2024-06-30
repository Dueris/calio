package io.github.apace100.calio;

import io.github.apace100.calio.util.CalioResourceConditions;
import net.fabricmc.api.ModInitializer;
import net.minecraft.advancement.criterion.Criteria;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.ApiStatus;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public class Calio implements ModInitializer {

    public static final Logger LOGGER = LogManager.getLogger(Calio.class);
	public static final String MOD_NAMESPACE = "calio";

	@ApiStatus.Internal
	public static final ThreadLocal<Set<String>> LOADED_NAMESPACES = new ThreadLocal<>();
	@ApiStatus.Internal
	public static final ThreadLocal<DynamicRegistryManager> DYNAMIC_REGISTRIES = new ThreadLocal<>();
	@ApiStatus.Internal
	public static final ThreadLocal<Map<TagKey<?>, Collection<RegistryEntry<?>>>> REGISTRY_TAGS = new ThreadLocal<>();

    public static Identifier identifier(String path) {
		return new Identifier(MOD_NAMESPACE, path);
	}

    @Override
	public void onInitialize() {
        Criteria.register(CodeTriggerCriterion.ID.toString(), CodeTriggerCriterion.INSTANCE);
		CalioResourceConditions.register();
	}

	public static boolean hasNonItalicName(ItemStack stack) {
		return stack.hasNbt() && stack.getSubNbt("display") != null && stack.getSubNbt("display").getBoolean(NbtConstants.NON_ITALIC_NAME);
	}

	public static void setNameNonItalic(ItemStack stack) {
		if(stack != null)
			stack.getOrCreateSubNbt("display").putBoolean(NbtConstants.NON_ITALIC_NAME, true);
	}

	public static boolean areEntityAttributesAdditional(ItemStack stack) {
		return stack.hasNbt() && stack.getNbt().contains(NbtConstants.ADDITIONAL_ATTRIBUTES) && stack.getNbt().getBoolean(NbtConstants.ADDITIONAL_ATTRIBUTES);
	}

	/**
	 * Sets whether the item stack counts the entity attribute modifiers specified in its tag as additional,
	 * meaning they won't overwrite the equipment's inherent modifiers.
	 * @param stack
	 * @param additional
	 */
	public static void setEntityAttributesAdditional(ItemStack stack, boolean additional) {
		if(stack != null) {
			if(additional) {
				stack.getOrCreateNbt().putBoolean(NbtConstants.ADDITIONAL_ATTRIBUTES, true);
			} else {
				if(stack.hasNbt()) {
					stack.getNbt().remove(NbtConstants.ADDITIONAL_ATTRIBUTES);
				}
			}
		}
	}

	public static <T> boolean areTagsEqual(RegistryKey<? extends Registry<T>> registryKey, TagKey<T> tag1, TagKey<T> tag2) {
		return areTagsEqual(tag1, tag2);
	}

	public static <T> boolean areTagsEqual(TagKey<T> tag1, TagKey<T> tag2) {
		if(tag1 == tag2) {
			return true;
		}
		if(tag1 == null || tag2 == null) {
			return false;
		}
		if(!tag1.registry().equals(tag2.registry())) {
			return false;
		}
		if(!tag1.id().equals(tag2.id())) {
			return false;
		}
		return true;
	}
}
