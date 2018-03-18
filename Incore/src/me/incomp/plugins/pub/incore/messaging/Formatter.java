package me.incomp.plugins.pub.incore.messaging;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author Incomp</br>
 * @since Jul 24, 2016</br>
 * </br>
 * This class is designed to simplify the process of creating presentable Strings
 * to send to whatever you want; Players or otherwise. Each method should have
 * elaborate documentation.</br>
 * </br>
 * No need for me to paste in some licensing jargon. You are more than welcome to
 * use and modify this for yourself. However, I'd like for my name to appear in
 * the credits. Can't let you forget how awesome I am.</br>
 * </br>
 * With that being said, enjoy programming.
 */
public class Formatter {
	private Formatter(){}; // No accidental construction
	
	public static final String DEBUG_PERM = "incore.debug";
	public static final String MAIN_PACKAGE = "me.incomp.plugins.pub.incore";
	
	/**
	 * Colors a string using the generic method. This is basically like a bit.ly link.
	 * Just shortens the code you have to write.
	 * 
	 * @param altColorChar	the character that will be used to tell Minecraft that
	 * 						the following character is a color code
	 * @param input			the input String whose altColorChars will be used for
	 * 						that thing I mentioned earlier.
	 * @return String
	 */
	public static String color(char altColorChar, String input){
		return ChatColor.translateAlternateColorCodes(altColorChar, input);
	}
	
	/**
	 * Automagically replaces ampersands (&) with Minecraft's color code
	 * characters.
	 * 
	 * @param input		input String
	 * @return String
	 */
	public static String color(String input){
		return Formatter.color('&', input);
	}
	
	/**
	 * Strips color from a String. My OCD demanded that I make this method.
	 * This method searches through a String for Minecraft's color code
	 * character. If it finds one, it will remove that along with the
	 * following character. Essentially, it takes the swag out of your
	 * text.
	 * 
	 * @param input		String to remove color from
	 * @return			nekkid String
	 */
	public static String stripColor(String input){
		return ChatColor.stripColor(input);
	}
	
	/**
	 * Uses your arguments to conjure a String that matches my formatting.
	 * It translates ampersands into color codes automagically.
	 * 
	 * @param header	Header of the message. Shouldn't be more than 1
	 * 					word in most cases
	 * @param body		Body of the message. The meaty meat of the message
	 * @param type		The parameter that determines the color of the header</br>
	 * 					</br>
	 * 					{@linkplain FormatType#NORMAL} gives a green header.</br>
	 * 					{@link FormatType#WARNING} gives a red header.</br>
	 * 					{@link FormatType#ERROR} gives a dark red header.</br>
	 * @return
	 */
	public static String format(String header, String body, FormatType type){
		return type.getChatColor() + color(header) + ChatColor.BOLD + " > " + ChatColor.GRAY + color(body) + ChatColor.RESET;
	}
	
	public static void log(String input, Level logLevel){
		Bukkit.getLogger().log(logLevel, Formatter.color(input));
	}
		
	public static String getLineRule(ChatColor color){
		return color + "" + ChatColor.STRIKETHROUGH + "-----------------------------------------------------";
	}
		
	public static String getLineRule(){
		return Formatter.getLineRule(ChatColor.DARK_GRAY);
	}
	
	public static void broadcast(String input){
		for(Player p : Bukkit.getOnlinePlayers()){
			p.sendMessage(color(input));
		}
	}
	
	public static void broadcast(List<String> input){
		for(Player p : Bukkit.getOnlinePlayers()){
			for(String s : input){
				p.sendMessage(color(s));
			}
		}
	}
	
	public static void broadcast(String input, String perm){
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.hasPermission(perm)){
				p.sendMessage(color(input));
			}
		}
	}
	
	public static void broadcast(List<String> input, String perm){
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.hasPermission(perm)){
				for(String s : input){
					p.sendMessage(color(s));
				}
			}else continue;
		}
	}
	
	public static List<String> wrap(String wrapper, int thickness, String... enclosed){
		return Formatter.wrap(wrapper, thickness, Arrays.asList(enclosed));
	}
	
	public static List<String> wrap(String wrapper, int thickness, List<String> enclosed){
		List<String> list = new ArrayList<>();
		for(int i = 0; i < thickness; i++){
			list.add(color(wrapper));
		}
		for(String s : enclosed){
			list.add(color(s));
		}
		for(int i = 0; i < thickness; i++){
			list.add(color(wrapper));
		}
		return list;
	}
	
	public static void clear(Player p, int amount){
		if(amount <= 0) return;
		for(int i = 0; i < amount; i++){
			p.sendMessage("");
		}
	}
	
	public static void clear(Player p){
		Formatter.clear(p, 128);
	}
	
	public static void clear(int amount){
		if(amount <= 0) return;
		for(int i = 0; i < amount; i++){
			Formatter.broadcast("");
		}
	}
	
	public static void clear(){
		Formatter.clear(128);
	}
	
	public static void debug(String msg){
		final String s = Formatter.color(msg);
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.hasPermission(DEBUG_PERM)) p.sendMessage(Formatter.format("Debug", msg, FormatType.ERROR));
		}
		System.out.println(Formatter.stripColor(s));
	}

	public static void debug(Player p, String msg){
		final String s = Formatter.color(msg);
		p.sendMessage(Formatter.format("Debug", s, FormatType.ERROR));
		System.out.println(Formatter.stripColor(s));
	}
	
	public static void sendExceptionDebug(Throwable ex){
		if(ex instanceof InvocationTargetException){ // Invocation can be multiple exceptions, this will grab the real one.
			ex = ((InvocationTargetException) ex).getTargetException();
		}
		StringBuilder sb = new StringBuilder("");
		sb.append(ex.getClass().getName() + ",");
		boolean relevant = false; // Checks if the error is relevant to this plugin. Maybe have a whitelist feature that allows for multiple class name checks.
		for(StackTraceElement ste : ex.getStackTrace()){
			String[] info = ste.getClassName().split("\\.");
			String clazz = info[info.length - 1];
			if(ste.getClassName().startsWith(MAIN_PACKAGE)){
				relevant = true;
				sb.append("at " + clazz + "." + ste.getMethodName() + "(" + ste.getLineNumber() + "),");
			}
		}
		if(!relevant) return;
		for(Player p : Bukkit.getOnlinePlayers()){
			if(p.hasPermission(DEBUG_PERM)){
				for(String str : sb.toString().split(",")){
					if(str.length() > 0){
						debug(p, str);
					}
				}
			}
		}
	}
	
	public static List<String> splitToList(String input, int length){
		List<String> out = new ArrayList<String>();
		Pattern pat = Pattern.compile("\\G\\s*(.{1," + length + "})(?=\\s|$)", Pattern.DOTALL);
		Matcher m = pat.matcher(input);
		while(m.find()){
			out.add(m.group(1));
		}
		return out;
	}
	
	public static List<String> splitToList(String input, int length, ChatColor color){
		List<String> out = new ArrayList<String>();
		input = Formatter.color(input);
		Pattern pat = Pattern.compile("\\G\\s*(.{1," + length + "})(?=\\s|$)", Pattern.DOTALL);
		Matcher m = pat.matcher(input);
		while(m.find()){
			out.add(color + color(m.group(1)));
		}
		return out;
	}
	
	public enum Generics {
		NO_PERMISSION(Formatter.format("Access", "That's not for you. Don't try to get involved, because there's no turning back.", FormatType.ERROR)),
		SENDER_PLAYER_ONLY(Formatter.format("Sender", "Only players can run this command.", FormatType.ERROR)),
		SENDER_CONSOLE_ONLY(Formatter.format("Sender", "This command can only be executed from the server terminal.", FormatType.ERROR)),
		SYNTAX_NOT_ENOUGH_ARGUMENTS(Formatter.format("Syntax", "Not enough arguments, nerd.", FormatType.ERROR)),
		SYNTAX_TOO_MANY_ARGUMENTS(Formatter.format("Syntax", "Too many arguments! Please be gentle. I am nothing more than a robot.", FormatType.ERROR)),
		SYNTAX_INVALID(Formatter.format("Syntax", "No dice. I can't be bothered to give you any specific reason for why that didn't work.", FormatType.ERROR)),
		SYNTAX_ARGUMENTS(Formatter.format("Syntax", "You might be sitting there and wondering \"Gosh diggity ding dong darnit, what do these brackets mean?\"."
				+ "\nDon't worry. I've got you covered."
				+ "\n\nThe angle brackets (< and >, in case you haven't passed the 2nd grade) surround arguments that are &crequired for the command to work&7."
				+ "\nThe square brackets (again, [ and ]) surround arguments that &acan be included or omitted&7."
				+ "\nPipes (this thing: |) separate &dalternative parameters for a single argument&7. For example, let's say a syntax goes like this:"
				+ "\n&e/command <a|b>"
				+ "\n&7You MUST include one of two choices: a or b. Easy, right?", FormatType.NORMAL));
		
		private final String message;
		
		private Generics(String message){
			this.message = message;
		}
		
		public String getMessage(){
			return this.message;
		}
	}
}