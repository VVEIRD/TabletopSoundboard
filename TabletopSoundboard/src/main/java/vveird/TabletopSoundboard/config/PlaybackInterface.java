package vveird.TabletopSoundboard.config;

public class PlaybackInterface 
{
	String name = null;
	int[] supportedChannels = null;
	
	public PlaybackInterface(String name, int[] supportedChannels)
	{
		this.name = name;
		this.supportedChannels = supportedChannels;
	}
	
	@Override
	public String toString() 
	{
		StringBuilder sb = new StringBuilder(this.name);
		sb.append("(");
		for (int i=0;i<supportedChannels.length;i++) {
			if (this.supportedChannels[i] == 2)
				sb.append("2");
			if (i<supportedChannels.length-1)
				sb.append(", ");
		}
		sb.append(")");
		return sb.toString();
	}
}
