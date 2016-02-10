package land.sebastianwie.shopware_uploadtool.transfer;

public class ProcessStatus {
	private int todo;
	private int done;
	private int errors;
	private long starttime;

	private boolean cancelled;

	public ProcessStatus(int todo) {
		this.todo = todo;
		this.done = 0;
		this.errors = 0;
		this.cancelled = false;
		// this.starttime = System.currentTimeMillis();
	}

	public void start() {
		this.starttime = System.currentTimeMillis();
	}

	public void cancel() {
		this.cancelled = true;
	}

	public boolean isCancelled() {
		return cancelled;
	}

	public int incDone() {
		return ++this.done;
	}

	public int incErrors() {
		return ++this.errors;
	}

	public int setDone(int done) {
		return this.done = done;
	}

	public int setErrors(int errors) {
		return this.errors = errors;
	}

	public int getDone() {
		return done;
	}

	public int getErrors() {
		return errors;
	}

	public void setTodo(int todo) {
		this.todo = todo;
	}

	public int getTodo() {
		return todo;
	}

	public void decTodo() {
		todo--;
	}

	public double getRatio() {
		return (double) done / (double) todo;
	}

	public long getStarttime() {
		return starttime;
	}

	public long getPassedTime() {
		return System.currentTimeMillis() - starttime;
	}

	public long getEstimatedTime() {
		return (long) (this.getPassedTime() * ((double) this.getTodo() / (double) this.getDone()));
	}

	public long getEstimatedRemainingTime() {
		return getEstimatedTime() - getPassedTime();
	}

}
