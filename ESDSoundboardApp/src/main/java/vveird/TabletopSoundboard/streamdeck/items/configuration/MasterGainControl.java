package vveird.TabletopSoundboard.streamdeck.items.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import de.rcblum.stream.deck.event.KeyEvent;
import de.rcblum.stream.deck.items.AbstractStreamItem;
import de.rcblum.stream.deck.util.IconHelper;
import vveird.TabletopSoundboard.AudioApp;

public class MasterGainControl {
	
	public static final MasterGainControl instance = new MasterGainControl();

	private Thread daemon = null;

	private Thread displayDaemon = null;
	
	private VolumeItem volItem = null;
	
	private int currentVolume = AudioApp.getVolume();
	
	private boolean volumeUp = false;
	
	private boolean volumeDown = false;
	
	private MasterGainControl() {
		this.daemon = new Thread(new Daemon());
		this.daemon.setDaemon(true);
		this.daemon.start();
		
		this.volItem = new VolumeItem();
		this.displayDaemon = new Thread(volItem);
		this.displayDaemon.setDaemon(true);
		this.displayDaemon.start();
	}
	
	public VolumeDownItem getVolumeDownItem() {
		return new VolumeDownItem();
	}
	
	public VolumeUpItem getVolumeUpItem() {
		return new VolumeUpItem();
	}
	
	public VolumeItem getVolumeDisplayItem() {
		return volItem;
	}
	
	public class VolumeDownItem extends AbstractStreamItem {

		public VolumeDownItem() {
			super(IconHelper.loadImageFromResource("/resources/icons/vol_down.png"));
		}

		@Override
		public void onKeyEvent(KeyEvent event) {
			switch(event.getType()) {
			case PRESSED:
				MasterGainControl.this.volumeDown = true;
				break;
			case RELEASED_CLICKED:
				MasterGainControl.this.volumeDown = false;
				break;
			default:
				break;
			}
		}
		
	}

	public class VolumeUpItem extends AbstractStreamItem {

		public VolumeUpItem() {
			super(IconHelper.loadImageFromResource("/resources/icons/vol_up.png"));
		}

		@Override
		public void onKeyEvent(KeyEvent event) {
			switch(event.getType()) {
			case PRESSED:
				MasterGainControl.this.volumeUp = true;
				break;
			case RELEASED_CLICKED:
				MasterGainControl.this.volumeUp = false;
				break;
			default:
				break;
			}
		}
		
	}

	public class VolumeItem extends AbstractStreamItem implements Runnable {

		private Logger logger = LogManager.getLogger(VolumeItem.class);
		
		private int vol = AudioApp.getVolume();

		public VolumeItem() {
			super(IconHelper.getImage("temp://BLACK_ICON"));
			this.setTextPosition(TEXT_POS_CENTER);
			this.setText("Volume: " + this.vol + "%");
		}

		@Override
		public void onKeyEvent(KeyEvent event) {
		}

		@Override
		public void run() {
			while(true) {
				try {
					if(MasterGainControl.this.currentVolume != this.vol) {
						this.vol = MasterGainControl.this.currentVolume;
						this.setText("Volume: " + this.vol + "%");
					}
					Thread.sleep(125);
				} catch (Exception e) {
					logger.error("Error adjusting volume");
					logger.error(e);
					e.printStackTrace();
				}
			}
		}
		
	}

	private class Daemon implements Runnable {
		
		private Logger logger = LogManager.getLogger(Daemon.class);

		@Override
		public void run() {
			long time = System.currentTimeMillis();
			while(true) {
				try {
					if ((volumeUp || volumeDown) && System.currentTimeMillis() - time > 400) {
						AudioApp.setVolume(AudioApp.getVolume() + (volumeUp ? 5 : -5));
						MasterGainControl.this.currentVolume = AudioApp.getVolume();
						if (volumeUp && MasterGainControl.this.currentVolume == 100) {
							volumeUp = false;
						}
						if (volumeDown && MasterGainControl.this.currentVolume == 0) {
							volumeDown = false;
						}
						time = System.currentTimeMillis();
					}
					Thread.sleep(10);
				} catch (Exception e) {
					logger.error("Error adjusting volume");
					logger.error(e);
					e.printStackTrace();
				}
			}
			
		}
		
	}
}
