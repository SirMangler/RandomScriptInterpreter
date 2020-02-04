package ChatBot;

import java.io.FileNotFoundException;

import javax.security.auth.login.LoginException;

import ScriptReader.ProcessScript;
import net.dv8tion.jda.core.AccountType;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

/**
 * @author SirMangler
 *
 * @date 27 Aug 2019
 */
public class DiscordWrap extends ListenerAdapter {

	public static void main(String[] args) {
		try {
			ProcessScript.loadScript();
			
			JDA jda = new JDABuilder(AccountType.BOT)
					.setToken(System.getenv("token")).buildBlocking();
		
			jda.addEventListener(new DiscordWrap());
		} catch (LoginException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}
	
	@Override
    public void onMessageReceived(MessageReceivedEvent event)
    {
        if (event.isFromType(ChannelType.PRIVATE))
        {
            System.out.printf("[PM] %s: %s\n", event.getAuthor().getName(),
                                    event.getMessage().getContentDisplay());
        }
        else
        {
            System.out.printf("[%s][%s] %s: %s\n", event.getGuild().getName(),
                        event.getTextChannel().getName(), event.getMember().getEffectiveName(),
                        event.getMessage().getContentDisplay());
            
            if (event.getMessage().getMentionedMembers().contains(event.getGuild().getSelfMember())) {
            	String input = event.getMessage().getContentDisplay();
            	
            	if (!input.contains(" ")) return;
            	
            	String response = Chat.processInput(input.substring(input.indexOf(" ")+1), event.getAuthor().getName());
            	
            	event.getChannel().sendMessage(response).queue();
            }
        }
    }
}
