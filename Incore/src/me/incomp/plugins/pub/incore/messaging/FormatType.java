package me.incomp.plugins.pub.incore.messaging;

import org.bukkit.ChatColor;

/**
 * @author Incomp
 * @since Jul 24, 2016
 */
public enum FormatType {
	/**
	 * For every message that doesn't classify as a {@link FormatType#WARNING} or
	 * {@link FormatType#ERROR}.
	 * 
	 */
	NORMAL(ChatColor.GREEN),
	
	/**
	 * Used for messages pertaining to errors in the plugin, or when things don't
	 * work properly. This includes debug messages and the like.
	 */
	ERROR(ChatColor.DARK_RED),
	
	/**
	 * Used for messages that are warnings. Things like "No permissions" should
	 * be using this header, along with the feedback from whenever a player
	 * attempts something but is expectedly prevented by the plugin. In other
	 * words, no errors.
	 */
	WARNING(ChatColor.RED);
	
	private final ChatColor color;
	
	private FormatType(ChatColor color){
		this.color = color;
	}
	
	/**
	 * Used to get the ChatColor associated with this FormatType.
	 * 
	 * @return ChatColor
	 */
	public ChatColor getChatColor(){
		return this.color;
	}
}
