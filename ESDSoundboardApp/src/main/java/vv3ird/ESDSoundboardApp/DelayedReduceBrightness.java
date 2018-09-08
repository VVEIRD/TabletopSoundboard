package vv3ird.ESDSoundboardApp;

import java.util.Objects;

import de.rcblum.stream.deck.device.IStreamDeck;
import de.rcblum.stream.deck.device.StreamDeckDevices;
import de.rcblum.stream.deck.event.KeyEvent;
import de.rcblum.stream.deck.event.StreamKeyListener;

public class DelayedReduceBrightness implements Runnable, StreamKeyListener{

	
	private long lastPressed = System.currentTimeMillis();
	
	private IStreamDeck streamDeck = null;
	
	public DelayedReduceBrightness(IStreamDeck streamDeck) {
		this.streamDeck = Objects.requireNonNull(streamDeck);
		Thread t = new Thread(this);
		t.start();
	}
	
	@Override
	public void run() {
		while(true) {
			 if (System.currentTimeMillis() - lastPressed > 20_000)
				 this.streamDeck.setBrightness(10);
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	@Override
	public void onKeyEvent(KeyEvent event) {
		lastPressed  = System.currentTimeMillis();
		this.streamDeck.setBrightness(75);
	}

}
