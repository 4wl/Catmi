package me.catmi.util.world;

public class Timer{
	private long current;
	private long time = -1L;


	public Timer(){
		this.current = System.currentTimeMillis();
	}

	public boolean hasReached(final long delay){
		return System.currentTimeMillis() - this.current >= delay;
	}

	public boolean hasReached(final long delay, boolean reset){
		if (reset)
			reset();
		return System.currentTimeMillis() - this.current >= delay;
	}

	public void reset(){
		this.current = System.currentTimeMillis();
	}

	public long getTimePassed(){
		return System.currentTimeMillis() - this.current;
	}

	public boolean sleep(final long time){
		if (time() >= time){
			reset();
			return true;
		}
		return false;
	}

	public boolean passedNS(long ns) {
		return (System.nanoTime() - this.time >= ns);
	}

	public long getPassedTimeMs() {
		return getMs(System.nanoTime() - this.time);
	}

	public boolean passedMs(long ms) {
		return (getMs(System.nanoTime() - this.time) >= ms);
	}

	public long getMs(long time) {
		return time / 1000000L;
	}

	public long time(){
		return System.currentTimeMillis() - current;
	}
}


