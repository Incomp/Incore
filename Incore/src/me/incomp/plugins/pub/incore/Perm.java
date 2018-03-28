package me.incomp.plugins.pub.incore;

import org.bukkit.permissions.Permissible;

/**
 * Contains all of the permission nodes used by Incore.
 * 
 * @author Incomp
 * @since Mar 10, 2018
 */
public enum Perm {
	
	RELOAD("incore.reload", "Required to reload the plugin."),
	PERMS("incore.perms", "Required to view your own permissions to Incore."),
	PERMS_OTHERS("incore.perms.others", "Required to view others' permissions to Incore."),
	MODULES_LIST("incore.modules", "Required to list all modules."),
	MODULES_MANAGE("incore.modules.manage", "Required to enable, disable, and reload modules."),
	
	MODULE_HEADDROP_RELOAD("incore.module.headdrop.reload", "Required to reload the Head Drop module."),
	
	MODULE_ITEMBAN_RELOAD("incore.module.itemban.reload", "Required to reload the Item Ban module."),
	MODULE_ITEMBAN_BYPASS("incore.module.itemban.bypass", "Required to bypass Item Ban's restrictions."),
	
	MODULE_PAY2CMD_BYPASS("incore.module.pay2cmd.bypass", "Required to bypass Pay2Cmd command costs.");
		
	private final String node;
	private final String desc;
	
	private Perm(String node, String desc) {
		this.node = node;
		this.desc = desc;
	}
	
	/**
	 * Used to get the full permission node. This is the method you'll probably be using the most.
	 * 
	 * @return String
	 */
	public String getNode() {
		return this.node;
	}
	
	/**
	 * Used to get the description of the permission node. I use this in the {@code /incore access} command.
	 * 
	 * @return String
	 */
	public String getDescription() {
		return this.desc;
	}
	
	/**
	 * Used to quickly check if a {@code Permissible} ({@code Player}, {@code ConsoleCommandSender}, {@code Cow}, etc.) has a permission node.
	 * 
	 * @param permissible		- Permissible to check
	 * @return {@code true} if {@code permissible} has the node;</br>
	 *         {@code false} otherwise
	 */
	public boolean has(Permissible permissible) {
		// NOTE: It may be necessary to check if the Permissible is an instance of ConsoleCommandSender and return true if so.
		return permissible.hasPermission(this.node);
	}
	
	/**
	 * Used to quickly check if a {@code Permissible} ({@code Player}, {@code ConsoleCommandSender}, {@code Cow}, etc.) has {@code perm.getNode()}.
	 * 
	 * @param permissible		- Permissible to check
	 * @param perm				- Perm to check for
	 * @return {@code true} if {@code permissible} has the node;</br>
	 *         {@code false} otherwise
	 */
	public static boolean has(Permissible permissible, Perm perm) {
		// NOTE: Same as before.
		return permissible.hasPermission(perm.getNode());
	}
}
