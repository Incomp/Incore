package me.incomp.plugins.pub.incore;

import org.bukkit.permissions.Permissible;

/**
 * Contains all of the permission nodes used by Incore.
 * 
 * @author Incomp
 * @since Mar 10, 2018
 */
public enum Perm {
	
	// General permissions.
	RELOAD("reload", "Required to reload the plugin."),
	PERMS("perms", "Required to view your own permissions."),
	PERMS_OTHERS("perms.others", "Required to view others' permissions."),
	
	// Head Drop module permissions.
	MODULE_HEADDROP_RELOAD("module.headdrop.reload", "Required to reload the Head Drop module."),
	
	// Item Ban module permissions.
	MODULE_ITEMBAN_RELOAD("module.itemban.reload", "Required to reload the Item Ban module."),
	MODULE_ITEMBAN_BYPASS("module.itemban.bypass", "Required to bypass item bans."),
	
	// General module permissions.
	MODULES_LIST("modules.list", "Required to list modules."),
	MODULES_MANAGE("modules.manage", "Required to enable, disable, and reload modules.");
	
	private final String key;
	private final String node;
	private final String desc;
	
	private Perm(String key, String desc) {
		this.key = key;
		this.node = "incore." + key;
		this.desc = desc;
	}
	
	/**
	 * Used to get the key of the permission node. This shouldn't ever really be used.
	 * 
	 * @return String
	 */
	public String getKey() {
		return this.key;
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
