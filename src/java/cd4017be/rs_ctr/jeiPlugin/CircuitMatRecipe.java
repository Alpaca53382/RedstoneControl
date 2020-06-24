package cd4017be.rs_ctr.jeiPlugin;

import java.util.*;
import java.util.Map.Entry;

import cd4017be.lib.util.ItemKey;
import cd4017be.lib.util.TooltipUtil;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.IRecipeWrapper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;


/**
 * @author CD4017BE
 *
 */
public class CircuitMatRecipe implements IRecipeWrapper {

	public final List<ItemStack> ingred;
	private final String[] info;

	public CircuitMatRecipe(Entry<ItemKey, int[]> entry) {
		this.ingred = Arrays.asList(entry.getKey().items);
		int[] stats = entry.getValue();
		int usage = Math.max(0, stats[0] + stats[1]);
		float burst = (float)stats[5] / (float)(usage - stats[4]);
		if (burst < 0) burst = Float.POSITIVE_INFINITY;
		this.info = TooltipUtil.format("gui.rs_ctr.assembler.stats", stats[0], stats[1], stats[2], stats[3], burst, (float)stats[4] / (float)usage * 20F).split("\n");
		if (Float.isInfinite(burst)) info[4] = TooltipUtil.format("gui.rs_ctr.assembler.stats4", stats[5]);
		if (usage == 0 && stats[4] != 0) info[5] = TooltipUtil.format("gui.rs_ctr.assembler.stats5", stats[4]);
	}

	@Override
	public void getIngredients(IIngredients ingredients) {
		ingredients.setInputLists(ItemStack.class, Collections.singletonList(ingred));
	}

	@Override
	public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
		FontRenderer fr = minecraft.fontRenderer;
		for (int i = 0; i < 6; i++) {
			String s = info[i];
			fr.drawString(s, 30 + ((i < 4 ? 26 : 48) - fr.getStringWidth(s)) / 2 + (i >> 1) * 40, 1 + (i & 1) * 9, 0xff808080);
		}
	}

	public static int compare(Entry<ItemKey, int[]> a, Entry<ItemKey, int[]> b) {
		int[] sa = a.getValue(), sb = b.getValue();
		int d = sa[0] + sa[1] + sa[2] - sb[0] - sb[1] - sb[2];
		for (int i = 0; d == 0 && i < sa.length; i++)
			d = sa[i] - sb[i];
		return d;
	}

}
