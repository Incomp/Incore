package me.incomp.plugins.pub.incore.messaging;

import org.bukkit.command.CommandSender;

/**
 * Contains preset messages.
 * 
 * @author Incomp
 * @since Mar 10, 2018
 */
public enum MsgPreset {

	// Errors
	NO_ACCESS("Access", "You do not have access to that command.", FormatType.ERROR),
	NO_PLAYER_FOUND("Target", "No player found by the name of &e\"%target%\"&7.", FormatType.ERROR);
	
	private final String header;
	private final String body;
	private final FormatType formatType;
	
	private MsgPreset(String hd, String bd, FormatType type) {
		this.header = hd;
		this.body = bd;
		this.formatType = type;
	}
	
	/**
	 * Gets the preset's header.
	 * 
	 * @return String
	 */
	public String getHeader() {
		return this.header;
	}
	
	/**
	 * Gets the preset's body.
	 * 
	 * @return String
	 */
	public String getBody() {
		return this.body;
	}
	
	/**
	 * Gets the preset's {@code FormatType}.
	 * 
	 * @return {@code FormatType}
	 */
	public FormatType getFormatType() {
		return this.formatType;
	}
	
	/**
	 * Gets the preset's whole message. This is probably the method you should use.
	 * 
	 * @return String
	 */
	public String get() {
		return Formatter.format(this.header, this.body, this.formatType);
	}
	
	/**
	 * Sends the preset's whole message to a specified {@code CommandSender}.
	 * 
	 * @param sn	- {@code CommandSender} to send message to
	 */
	public void send(CommandSender sn) {
		sn.sendMessage(Formatter.format(this.header, this.body, this.formatType));
	}
}
